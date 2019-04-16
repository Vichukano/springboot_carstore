package ru.job4j.carprice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.carprice.dto.CarDto;
import ru.job4j.carprice.dto.FormDto;
import ru.job4j.carprice.model.*;
import ru.job4j.carprice.service.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
public class CarControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private CarService carService;
    @MockBean
    private CarBodyService bodyService;
    @MockBean
    private EngineService engineService;
    @MockBean
    private TransmissionService trService;
    @MockBean
    private UserService userService;
    @MockBean
    private Principal principal;
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "test", password = "test")
    public void whenGetAllCarsThenReturnListOfCarsInJson() throws Exception {
        when(this.principal.getName()).thenReturn("test");
        when(this.userService.findByLogin("test")).thenReturn(new User(1L));
        when(this.carService.init()).thenReturn(this.carService);
        when(this.carService.action(Action.Type.ALL)).thenReturn(
                Arrays.asList(
                        new Car("test1", 1D),
                        new Car("test2", 2D),
                        new Car("test3", 3D)
                )
        );
        this.mvc.perform(
                get("/api/cars")
                        .param("action", "ALL")
                        .principal(this.principal)
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().string(
                        "[{\"id\":0,\"name\":\"test1\",\"price\":1.0,\"color\":null,\"mileage\""
                                + ":0,\"sold\":false,\"description\":null,\"createDate\":null,\"body\":null,\"engine\":null,"
                                + "\"transmission\":null,\"image\":null,\"user\":null},{\"id\":0,\"name\":\"test2\",\"price\""
                                + ":2.0,\"color\":null,\"mileage\":0,\"sold\":false,\"description\":null,\"createDate\":null,\""
                                + "body\":null,\"engine\":null,\"transmission\":null,\"image\":null,\"user\":null},{\"id\":0,\""
                                + "name\":\"test3\",\"price\":3.0,\"color\":null,\"mileage\":0,\"sold\":false,\"description\""
                                + ":null,\"createDate\":null,\"body\":null,\"engine\":null,\"transmission\":null,\"image\":null,"
                                + "\"user\":null}]"
                )
        );
        verify(this.carService, times(1)).init();
        verify(this.carService, times(1)).action(Action.Type.ALL);
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void whenGetAllCarsWithImageThenReturnEmptyList() throws Exception {
        when(this.principal.getName()).thenReturn("test");
        when(this.userService.findByLogin("test")).thenReturn(new User(1L));
        when(this.carService.init()).thenReturn(this.carService);
        when(this.carService.action(Action.Type.IMAGE)).thenReturn(new ArrayList<>());
        this.mvc.perform(
                get("/api/cars")
                        .param("action", "IMAGE")
                        .principal(this.principal)
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().string("[]")
        );
        verify(this.carService, times(1)).init();
        verify(this.carService, times(1)).action(Action.Type.IMAGE);
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void whenGetAllRelevantCarsThenReturnCarsInJson() throws Exception {
        List<Car> cars = new ArrayList<>();
        Car ford = new Car("ford", 1D);
        Car toyota = new Car("toyota", 2D);
        cars.add(ford);
        cars.add(toyota);
        when(this.principal.getName()).thenReturn("test");
        when(this.userService.findByLogin("test")).thenReturn(new User(1L));
        when(this.carService.init()).thenReturn(this.carService);
        when(this.carService.action(Action.Type.RELEVANT)).thenReturn(cars);
        this.mvc.perform(
                get("/api/cars")
                        .param("action", "RELEVANT")
                        .principal(this.principal)
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().string(
                        "[{\"id\":0,\"name\":\"ford\",\"price\":1.0,\"color\":null,\"mileage\""
                                + ":0,\"sold\":false,\"description\":null,\"createDate\":null,\"body\":null,\"engine\":null,\""
                                + "transmission\":null,\"image\":null,\"user\":null},{\"id\":0,\"name\":\"toyota\",\"price\":2.0,\""
                                + "color\":null,\"mileage\":0,\"sold\":false,\"description\":null,\"createDate\":null,"
                                + "\"body\":null,\"engine\":null,\"transmission\":null,\"image\":null,\"user\":null}]"
                )
        );
        verify(this.carService, times(1)).init();
        verify(this.carService, times(1)).action(Action.Type.RELEVANT);
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void whenGetAllCarsForLastDayThenReturnCarsInJson() throws Exception {
        when(this.principal.getName()).thenReturn("test");
        when(this.userService.findByLogin("test")).thenReturn(new User(1L));
        when(this.carService.init()).thenReturn(this.carService);
        when(this.carService.action(Action.Type.LAST)).thenReturn(
                Arrays.asList(
                        new Car("test1", 1D),
                        new Car("test2", 2D),
                        new Car("test3", 3D)
                )
        );
        this.mvc.perform(
                get("/api/cars")
                        .param("action", "LAST")
                        .principal(this.principal)
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().string(
                        "[{\"id\":0,\"name\":\"test1\",\"price\":1.0,\"color\":null,\"mileage\""
                                + ":0,\"sold\":false,\"description\":null,\"createDate\":null,\"body\":null,\"engine\":null,"
                                + "\"transmission\":null,\"image\":null,\"user\":null},{\"id\":0,\"name\":\"test2\",\"price\""
                                + ":2.0,\"color\":null,\"mileage\":0,\"sold\":false,\"description\":null,\"createDate\":null,\""
                                + "body\":null,\"engine\":null,\"transmission\":null,\"image\":null,\"user\":null},{\"id\":0,\""
                                + "name\":\"test3\",\"price\":3.0,\"color\":null,\"mileage\":0,\"sold\":false,\"description\""
                                + ":null,\"createDate\":null,\"body\":null,\"engine\":null,\"transmission\":null,\"image\":null,"
                                + "\"user\":null}]"
                )
        );
        verify(this.carService, times(1)).init();
        verify(this.carService, times(1)).action(Action.Type.LAST);
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void whenGetAllCarsAndFindByPartThenReturnListOfCars() throws Exception {
        when(this.principal.getName()).thenReturn("test");
        when(this.userService.findByLogin("test")).thenReturn(new User(1L));
        when(this.carService.init()).thenReturn(this.carService);
        when(this.carService.findCarByPart("test", "test")).thenReturn(
                Arrays.asList(new Car("test", 1D))
        );
        this.mvc.perform(
                get("/api/cars")
                        .param("query", "test")
                        .param("type", "test")
                        .principal(this.principal)
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().string(
                        "[{\"id\":0,\"name\":\"test\",\"price\":1.0,\"color\":null,\"mileage\":0,"
                                + "\"sold\":false,\"description\":null,\"createDate\":null,\"body\":null,\"engine\":null,"
                                + "\"transmission\":null,\"image\":null,\"user\":null}]"
                )
        );
        verify(this.carService, times(1)).findCarByPart("test", "test");
        verify(this.carService, times(0)).init();
        verify(this.carService, times(0)).action(any());
    }

    /**
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "test", password = "test")
    public void whenAddNewCarWithoutImageThenStoreIt() throws Exception {
        CarDto dto = mock(CarDto.class);
        when(this.userService.findById(1L)).thenReturn(new User("test", "test"));
        when(dto.getBody()).thenReturn(1L);
        when(dto.getEngine()).thenReturn(1L);
        when(dto.getTransmission()).thenReturn(1L);
        when(dto.getColor()).thenReturn("black");
        when(dto.getName()).thenReturn("test car");
        when(dto.getMileage()).thenReturn(100500);
        when(dto.getPrice()).thenReturn(100500D);
        when(dto.getDesc()).thenReturn("test desc");
        when(this.bodyService.findById(1L)).thenReturn(new CarBody("sedan"));
        when(this.engineService.findById(1L)).thenReturn(new Engine("petrol"));
        when(this.trService.findById(1L)).thenReturn(new Transmission("mechanical"));
        doNothing().when(this.carService).add(any(Car.class));
        this.mvc.perform(
                post("/api/cars")
                        .flashAttr("carDto", dto)
                        .sessionAttr("id", "1")
        ).andExpect(
                status().is3xxRedirection()
        ).andExpect(
                redirectedUrl("/")
        );
        verify(this.carService, times(1)).add(any(Car.class));
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void whenGetCarBodiesThenReturnListOfBodies() throws Exception {
        List<CarBody> carBodies = Arrays.asList(
                new CarBody("test1"),
                new CarBody("test2"),
                new CarBody("test3")
        );
        when(this.bodyService.findAll()).thenReturn(carBodies);
        this.mvc.perform(
                get("/api/body")
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                content().string(this.mapper.writeValueAsString(carBodies))
        );
        verify(this.bodyService, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void whenGetCarEnginesThenReturnListOfEngines() throws Exception {
        List<Engine> engines = Arrays.asList(
                new Engine("test1"),
                new Engine("test2"),
                new Engine("test3")
        );
        when(this.engineService.findAll()).thenReturn(engines);
        this.mvc.perform(
                get("/api/engine")
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                content().string(this.mapper.writeValueAsString(engines))
        );
        verify(this.engineService, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void whenGetCarTransmissionThenReturnListOfTransmissions() throws Exception {
        List<Transmission> transmissions = Arrays.asList(
                new Transmission("test1"),
                new Transmission("test2"),
                new Transmission("test3")
        );
        when(this.trService.findAll()).thenReturn(transmissions);
        this.mvc.perform(
                get("/api/transmission")
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                content().string(this.mapper.writeValueAsString(transmissions))
        );
        verify(this.trService, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void whenGetCarForUpdateThenReturnCar() throws Exception {
        Car car = new Car("test", 100500D);
        when(this.carService.findById(any(Car.class))).thenReturn(car);
        this.mvc.perform(
                get("/api/update")
                        .param("id", "1")
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(
                content().string(this.mapper.writeValueAsString(car))
        );
        verify(this.carService, times(1)).findById(any(Car.class));
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void whenUpdateCarThenUpdateAndStoreIt() throws Exception {
        FormDto dto = mock(FormDto.class);
        when(dto.getCarId()).thenReturn(1);
        when(dto.getBody()).thenReturn(1L);
        when(dto.getEngine()).thenReturn(1L);
        when(dto.getTransmission()).thenReturn(1L);
        when(dto.getColor()).thenReturn("test");
        when(dto.getDesc()).thenReturn("test");
        when(dto.getName()).thenReturn("test");
        when(dto.getMileage()).thenReturn(100500);
        when(dto.getPrice()).thenReturn(100500D);
        when(dto.getSold()).thenReturn("on sale");
        when(this.carService.findById(any(Car.class))).thenReturn(new Car());
        when(this.bodyService.findById(1L)).thenReturn(new CarBody("test"));
        when(this.engineService.findById(1L)).thenReturn(new Engine("test"));
        when(this.trService.findById(1L)).thenReturn(new Transmission("test"));
        this.mvc.perform(
                post("/api/update")
                        .flashAttr("dto", dto)
        ).andExpect(
                status().is3xxRedirection()
        ).andExpect(
                redirectedUrl("/")
        );
        verify(this.carService, times(1)).update(any(Car.class));
    }
}
