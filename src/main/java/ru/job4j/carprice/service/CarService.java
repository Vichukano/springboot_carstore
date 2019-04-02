package ru.job4j.carprice.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.carprice.model.Car;
import ru.job4j.carprice.persistence.repository.CarRepository;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Class for service methods with Car object.
 * Singleton by default.
 */
@Service
public class CarService {
    private final CarRepository repository;
    private final Map<Action.Type, Supplier<List<Car>>> dispatch = new HashMap<>();
    private final Logger logger = LogManager.getLogger(CarService.class);

    @Autowired
    public CarService(CarRepository repository) {
        this.repository = repository;
    }

    public void add(Car car) {
        this.repository.save(car);
        logger.debug("Added car: {}", car.toString());
    }

    public void delete(Car car) {
        this.repository.delete(car);
        logger.debug("Car with id={} deleted.", car.getId());
    }

    public void update(Car car) {
        this.repository.save(car);
        logger.debug("Car with id={} updated.", car.getId());
    }

    public Car findById(Car car) {
        return this.repository
                .findById(car.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(car.getId())));
    }

    private List<Car> findAll() {
        return (List<Car>) this.repository.findAll();
    }

    private List<Car> findCarWithImage() {
        return this.repository.findCarWithImage();
    }

    private List<Car> findCarForLastDay() {
        return this.repository.findCarForLastDay();
    }

    private List<Car> findRelevantCars() {
        return this.repository.findRelevant();
    }

    private List<Car> findCarByBody(String type) {
        return this.repository.findCarByBody(type);
    }

    private List<Car> findCarByEngine(String type) {
        return this.repository.findCarByEngine(type);
    }

    private List<Car> findCarByTransmission(String type) {
        return this.repository.findCarByTransmission(type);
    }

    public List<Car> findCarByPart(String query, String type) {
        switch (query) {
            case ("findCarByBody"):
                return this.findCarByBody(type);
            case ("findCarByEngine"):
                return this.findCarByEngine(type);
            case ("findCarByTransmission"):
                return this.findCarByTransmission(type);
            default:
                logger.debug("Unsupported query: {}", query);
                return null;
        }
    }

    private Supplier<List<Car>> getAll() {
        return this::findAll;
    }

    private Supplier<List<Car>> withImage() {
        return this::findCarWithImage;
    }

    private Supplier<List<Car>> forLastDay() {
        return this::findCarForLastDay;
    }

    private Supplier<List<Car>> findRelevant() {
        return this::findRelevantCars;
    }

    public CarService init() {
        this.load(Action.Type.ALL, getAll());
        this.load(Action.Type.IMAGE, withImage());
        this.load(Action.Type.LAST, forLastDay());
        this.load(Action.Type.RELEVANT, findRelevant());
        return this;
    }

    private void load(Action.Type type, Supplier<List<Car>> handle) {
        this.dispatch.put(type, handle);
    }

    public List<Car> action(Action.Type type) {
        return this.dispatch.get(type).get();
    }
}
