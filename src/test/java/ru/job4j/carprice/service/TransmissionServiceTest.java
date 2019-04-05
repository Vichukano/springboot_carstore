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
public class TransmissionServiceTest {

    @Autowired
    private TransmissionService service;

    @Test
    public void whenFindAllThenReturnListOfCarBodies() {
        assertThat(this.service.findAll().size(), is(3));
        assertThat(this.service.findAll().get(0).getType(), is("mechanical"));
        assertThat(this.service.findAll().get(1).getType(), is("automatic"));
        assertThat(this.service.findAll().get(2).getType(), is("robot"));
    }

    @Test
    public void whenFindByIdThenReturnCarBodyWithThisId() {
        assertThat(this.service.findById(1L).getType(), is("mechanical"));
        assertThat(this.service.findById(2L).getType(), is("automatic"));
        assertThat(this.service.findById(3L).getType(), is("robot"));
    }
}
