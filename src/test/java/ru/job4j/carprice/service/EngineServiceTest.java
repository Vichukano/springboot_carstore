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
public class EngineServiceTest {

    @Autowired
    private EngineService service;

    @Test
    public void whenFindAllThenReturnListOfEngines() {
        assertThat(this.service.findAll().size(), is(3));
        assertThat(this.service.findAll().get(0).getType(), is("petrol"));
        assertThat(this.service.findAll().get(1).getType(), is("diesel"));
        assertThat(this.service.findAll().get(2).getType(), is("hybrid"));
    }

    @Test
    public void whenFindByIdThenReturnCarBodyWithThisId() {
        assertThat(this.service.findById(1L).getType(), is("petrol"));
        assertThat(this.service.findById(2L).getType(), is("diesel"));
        assertThat(this.service.findById(3L).getType(), is("hybrid"));
    }
}
