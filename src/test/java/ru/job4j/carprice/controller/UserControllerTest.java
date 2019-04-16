package ru.job4j.carprice.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import ru.job4j.carprice.model.User;
import ru.job4j.carprice.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;

    @Test
    @WithMockUser(username = "test", password = "test")
    public void whenRegNewUserThenStoreItAndRedirect() throws Exception {
        this.mvc.perform(
                post("/api/reg")
                        .param("login", "test")
                        .param("password", "test")
                        .param("confirm", "test")
        ).andExpect(
                status().is3xxRedirection()
        ).andExpect(
                redirectedUrl("/")
        );
        verify(this.service, times(1))
                .add(new User("test", "test"));
        verify(this.service, times(1))
                .isExist(new User("test", "test"));
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void whenLogoutThenRedirectToLoginPage() throws Exception {
        this.mvc.perform(
                get("/api/logout")
        ).andExpect(
                status().is3xxRedirection()
        ).andExpect(
                redirectedUrl("/login")
        );
    }


}
