package ru.job4j.carprice.controller;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@PrepareForTest(ImageController.class)
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@PowerMockIgnore({"javax.management.*", "javax.script.*", "javax.net.ssl.*"})
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
public class ImageControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "test", password = "test")
    public void whenLoadEmptyImageThenReturnAndSendOkStatus() throws Exception {
        this.mvc.perform(
                get("/api/image/empty")
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType("image/jpg")
        );
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void whenLoadImageThenSendItToClient() throws Exception {
        final ServletOutputStream outputStream = PowerMockito.mock(ServletOutputStream.class);
        final BufferedInputStream bin = PowerMockito.mock(BufferedInputStream.class);
        final FileInputStream fin = PowerMockito.mock(FileInputStream.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        whenNew(FileInputStream.class).withParameterTypes(String.class).withArguments(anyString()).thenReturn(fin);
        whenNew(BufferedInputStream.class).withArguments(fin).thenReturn(bin);
        when(resp.getOutputStream()).thenReturn(outputStream);
        when(bin.read()).thenReturn(2).thenReturn(3).thenReturn(-1);
        this.mvc.perform(
                get("/api/image/test")
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType("image/jpg")
        );
        verify(bin, times(3)).read();
    }
}
