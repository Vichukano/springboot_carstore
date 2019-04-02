package ru.job4j.carprice.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.carprice.dto.CarDto;
import ru.job4j.carprice.dto.FormDto;
import ru.job4j.carprice.model.*;
import ru.job4j.carprice.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.security.Principal;
import java.util.List;
import java.util.Random;

/**
 * Controller for Car and Car parts objects.
 */
@RestController
public class CarController {
    private final CarService carService;
    private final CarBodyService carBodyService;
    private final EngineService engineService;
    private final TransmissionService transmissionService;
    private final UserService userService;
    private final Random random = new Random();
    private final Logger logger = LogManager.getLogger(CarController.class);
    private final String path;

    @Autowired
    public CarController(
            CarService carService,
            CarBodyService carBodyService,
            EngineService engineService,
            TransmissionService transmissionService,
            UserService userService,
            @Value("${image.path}") String path
    ) {
        this.carService = carService;
        this.carBodyService = carBodyService;
        this.engineService = engineService;
        this.transmissionService = transmissionService;
        this.userService = userService;
        this.path = path;
    }

    /**
     * Method for sending list of cars entities in json
     * format to the view.
     *
     * @param action value of action in String format.
     * @param query  value of NamedQuery in String format.
     * @param type   value of type for NamedQuery in String format.
     * @return list of car entities in jason format.
     */
    @GetMapping(value = "/api/cars")
    public List<Car> getAllCars(@RequestParam(required = false) String action,
                                @RequestParam(required = false) String query,
                                @RequestParam(required = false) String type,
                                HttpServletRequest req,
                                Principal principal) {
        User user = this.userService.findByLogin(principal.getName());
        HttpSession session = req.getSession();
        session.setAttribute("id", user.getId());
        session.setAttribute("login", user.getLogin());
        logger.debug(
                "Session attributes: {}, {}",
                session.getAttribute("id"),
                session.getAttribute("login")
        );
        List<Car> cars;
        if (action != null) {
            Action.Type actionType = Action.Type.valueOf(
                    action.toUpperCase()
            );
            logger.debug("Action is: {}", actionType.toString());
            cars = this.carService.init().action(actionType);
        } else {
            logger.debug("Query: {}, type: {}", query, type);
            cars = this.carService.findCarByPart(query, type);
        }
        logger.debug("Cars from client: {}", cars);
        return cars;
    }

    /**
     * DONE
     *
     * @param file
     * @param req
     * @param resp
     * @throws IOException
     */
    @PostMapping(value = "/api/cars")
    public void addNewCar(@RequestParam(name = "image", required = false) MultipartFile file,
                          CarDto carDto,
                          HttpServletRequest req,
                          HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession();
        Image image = null;
        Car car = null;
        if (!file.isEmpty()) {
            byte[] buffer = file.getBytes();
            String path = this.path
                    + random.nextInt(1000)
                    + file.getName();
            try (BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream(new File(path))
            )) {
                out.write(buffer);
                image = new Image();
                image.setUrl(path);
                logger.debug("Image with path: {}", image.getUrl());
            } catch (IOException e) {
                logger.debug("IOException", e);
            }
        }
        long id = Long.parseLong(String.valueOf(session.getAttribute("id")));
        logger.debug("Id session attribute: {}", session.getAttribute("id"));
        User user = this.userService.findById(id);
        logger.debug("Found user: {}", user.toString());
        CarBody body = this.carBodyService.findById(carDto.getBody());
        logger.debug("Found body: {}", body.toString());
        Engine engine = this.engineService.findById(carDto.getEngine());
        logger.debug("Found engine: {}", engine.toString());
        Transmission tr = this.transmissionService.findById(carDto.getTransmission());
        logger.debug("Found tr: {}", tr.toString());
        car = new Car(
                carDto.getName(),
                carDto.getPrice(),
                carDto.getColor(),
                body,
                engine,
                tr,
                carDto.getMileage()
        );
        car.setDescription(carDto.getDesc());
        car.setUser(user);
        if (image == null) {
            image = new Image("empty");
        }
        car.setImage(image);
        this.carService.add(car);
        logger.debug("User with login {} add new car",
                session.getAttribute("login"));
        logger.debug("Car from client: {}", car);
        resp.sendRedirect("/");
    }


    /**
     * Method return CarBody entities in json format to view.
     *
     * @return List of CarBody entities in json format.
     */
    @GetMapping(value = "/api/body")
    public List<CarBody> getCarBodies() {
        List<CarBody> bodies = this.carBodyService.findAll();
        logger.debug("Car bodies: {} ", bodies);
        return bodies;
    }

    /**
     * Method return Engine entities in json format to view.
     *
     * @return List of Engine entities in json format.
     */
    @GetMapping(value = "/api/engine")
    public List<Engine> getCarEngines() {
        List<Engine> engines = this.engineService.findAll();
        logger.debug("Car engines: {}", engines);
        return engines;
    }

    /**
     * Method return Transmission entities in json format to view.
     *
     * @return List of Transmission entities in json format.
     */
    @GetMapping(value = "/api/transmission")
    public List<Transmission> getCarTransmissions() {
        List<Transmission> transmissions = this.transmissionService.findAll();
        logger.debug("Car transmissions: {}", transmissions);
        return transmissions;
    }


    /**
     * Method get id of car for update in request param,
     * found car with this id in database and
     * sent it to client in json format.
     *
     * @param id id of car for update.
     * @return car in json format.
     */
    @GetMapping(value = "/api/update")
    public Car getCarForUpdate(@RequestParam String id) {
        logger.debug("Car id from client: {}", id);
        Car car = new Car();
        car.setId(Long.parseLong(id));
        return this.carService.findById(car);
    }

    /**
     * Method for update car params.
     * Get request params from client and transfer it
     * to FormDto object(POJO). After update redirect to
     * index.html page.
     *
     * @throws IOException
     */
    @PostMapping(value = "/api/update")
    public void updateCar(FormDto dto, HttpServletResponse resp)
            throws IOException {
        logger.debug("Car id for update: {}", dto.getCarId());
        CarBody carBody = this.carBodyService.findById(dto.getBody());
        Engine carEngine = this.engineService.findById(dto.getEngine());
        Transmission tr = transmissionService.findById(dto.getTransmission());
        Car car = new Car();
        car.setId(dto.getCarId());
        Car found = this.carService.findById(car);
        found.setSold(true);
        if (dto.getSold().equals("on sale")) {
            found.setSold(false);
        }
        found.setName(dto.getName());
        found.setPrice(dto.getPrice());
        found.setColor(dto.getColor());
        found.setMileage(dto.getMileage());
        found.setBody(carBody);
        found.setEngine(carEngine);
        found.setTransmission(tr);
        found.setDescription(dto.getDesc());
        this.carService.update(found);
        logger.debug("Car updated: {}", found);
        resp.sendRedirect("/");
    }
}
