package ru.job4j.carprice.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.job4j.carprice.model.Car;
import ru.job4j.carprice.model.Image;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class CarServiceTest {

    @Autowired
    private CarService service;

    @Autowired
    private CarBodyService bodyService;

    @Autowired
    private EngineService engineService;

    @Autowired
    private TransmissionService trService;

    @Autowired
    private UserService userService;

    @Test
    public void whenFindAllThenReturnListOfCars() {
        List<Car> cars = this.service.init().action(Action.Type.ALL);
        assertThat(cars.size(), is(3));
    }

    @Test
    public void whenFindCarByIdThenReturnCar() {
        Car car = new Car();
        car.setId(1L);
        Car found = this.service.findById(car);
        assertThat(found.getId(), is(1L));
        assertThat(found.getName(), is("FORD"));
        assertThat(found.getColor(), is("RED"));
        assertThat(found.getDescription(), is("PERFECT CAR!"));
    }

    @Test
    public void whenAddAndDeleteCarThenStoreAndDeleteIt() {
        Car added = new Car(
                "New Car",
                100500D,
                "METALLIC",
                this.bodyService.findById(1L),
                this.engineService.findById(1L),
                this.trService.findById(1L),
                100500
        );
        added.setImage(new Image("empty"));
        added.setUser(this.userService.findById(1L));
        this.service.add(added);
        List<Car> cars = this.service.init().action(Action.Type.ALL);
        assertThat(cars.size(), is(4));
        assertThat(cars.get(3).getName(), is("New Car"));
        Car carForDelete = this.service.findById(cars.get(3));
        this.service.delete(carForDelete);
        cars = this.service.init().action(Action.Type.ALL);
        assertThat(cars.size(), is(3));
    }

    @Test
    public void whenUpdateCarThenUpdateItInDatabase() {
        Car beforeUpdate = this.service.findById(new Car(1L));
        assertThat(beforeUpdate.getName(), is("FORD"));
        beforeUpdate.setName("UPDATED MODEL");
        this.service.update(beforeUpdate);
        Car afterUpdate = this.service.findById(new Car(1L));
        assertThat(afterUpdate.getName(), is("UPDATED MODEL"));
        afterUpdate.setName("FORD");
        afterUpdate.setSold(false);
        assertThat(afterUpdate.getId(), is(1L));
        this.service.update(afterUpdate);
    }

    @Test
    public void whenFindRelevantThenReturnRelevantCars() {
        List<Car> cars = this.service.init().action(Action.Type.RELEVANT);
        assertThat(cars.size(), is(2));
        assertThat(cars.get(0).isSold(), is(false));
        assertThat(cars.get(1).isSold(), is(false));
    }

    @Test
    public void whenFindCarsWithImageThenReturnCarsWithImage() {
        List<Car> cars = this.service.init().action(Action.Type.IMAGE);
        assertThat(cars.size(), is(0));
    }

    @Test
    public void whenFindBySedanBodyThenReturnListOfCars() {
        List<Car> cars = this.service.findCarByPart("findCarByBody", "sedan");
        assertThat(cars.size(), is(1));
        assertThat(cars.get(0).getBody().getType(), is("sedan"));
    }

    @Test
    public void whenFindByHatchbackBodyThenReturnListOfCars() {
        List<Car> cars = this.service.findCarByPart("findCarByBody", "hatchback");
        assertThat(cars.size(), is(1));
        assertThat(cars.get(0).getBody().getType(), is("hatchback"));
    }

    @Test
    public void whenFindByCrossoverBodyThenReturnListOfCars() {
        List<Car> cars = this.service.findCarByPart("findCarByBody", "crossover");
        assertThat(cars.size(), is(1));
        assertThat(cars.get(0).getBody().getType(), is("crossover"));
    }

    @Test
    public void whenFindByGasolineEngineThenReturnListOfCars() {
        List<Car> cars = this.service.findCarByPart("findCarByEngine", "petrol");
        assertThat(cars.size(), is(1));
        assertThat(cars.get(0).getEngine().getType(), is("petrol"));
    }

    @Test
    public void whenFindByDieselEngineThenReturnListOfCars() {
        List<Car> cars = this.service.findCarByPart("findCarByEngine", "diesel");
        assertThat(cars.size(), is(1));
        assertThat(cars.get(0).getEngine().getType(), is("diesel"));
    }

    @Test
    public void whenFindByHybridEngineThenReturnListOfCars() {
        List<Car> cars = this.service.findCarByPart("findCarByEngine", "hybrid");
        assertThat(cars.size(), is(1));
        assertThat(cars.get(0).getEngine().getType(), is("hybrid"));
    }

    @Test
    public void whenFindByMechanicalTransmissionThenReturnListOfCars() {
        List<Car> cars = this.service.findCarByPart("findCarByTransmission", "mechanical");
        assertThat(cars.size(), is(1));
        assertThat(cars.get(0).getTransmission().getType(), is("mechanical"));
    }

    @Test
    public void whenFindByAutomaticTransmissionThenReturnListOfCars() {
        List<Car> cars = this.service.findCarByPart("findCarByTransmission", "automatic");
        assertThat(cars.size(), is(1));
        assertThat(cars.get(0).getTransmission().getType(), is("automatic"));
    }

    @Test
    public void whenFindByRobotTransmissionThenReturnListOfCars() {
        List<Car> cars = this.service.findCarByPart("findCarByTransmission", "robot");
        assertThat(cars.size(), is(1));
        assertThat(cars.get(0).getTransmission().getType(), is("robot"));
    }
}
