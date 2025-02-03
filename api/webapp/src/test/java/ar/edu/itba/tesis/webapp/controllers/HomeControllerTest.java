package ar.edu.itba.tesis.webapp.controllers;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @InjectMocks
    private HomeController homeController;

    @Test
    void testHello() {
        Response response = homeController.hello();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Welcome to this API REST", response.getEntity());
    }
}