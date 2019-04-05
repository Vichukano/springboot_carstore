package ru.job4j.carprice.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class CarBodyServiceTest {

    @Autowired
    private CarBodyService service;

    @Test
    public void whenFindAllThenReturnListOfCarBodies() {
        assertThat(this.service.findAll().size(), is(3));
        assertThat(this.service.findAll().get(0).getType(), is("sedan"));
        assertThat(this.service.findAll().get(1).getType(), is("hatchback"));
        assertThat(this.service.findAll().get(2).getType(), is("crossover"));
    }

    @Test
    public void whenFindByIdThenReturnCarBodyWithThisId() {
        assertThat(this.service.findById(1L).getType(), is("sedan"));
        assertThat(this.service.findById(2L).getType(), is("hatchback"));
        assertThat(this.service.findById(3L).getType(), is("crossover"));
    }
}
