package ar.edu.itba.tesis.webapp.config;

import ar.edu.itba.tesis.webapp.controllers.DetectorController;
import ar.edu.itba.tesis.webapp.controllers.HomeController;
import ar.edu.itba.tesis.webapp.controllers.SignalController;
import ar.edu.itba.tesis.webapp.controllers.UserController;
import ar.edu.itba.tesis.webapp.exceptionMappers.*;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/")
public class JerseyConfiguration extends ResourceConfig {

    @PostConstruct
    public void init() {
        registerClasses(
                DetectorController.class,
                HomeController.class,
                SignalController.class,
                UserController.class
        );

        registerClasses(
                AlreadyExistsExceptionMapper.class,
                ClientErrorExceptionMapper.class,
                ConstraintViolationExceptionMapper.class,
                GenericExceptionMapper.class,
                NotFoundExceptionMapper.class
        );

        register(ObjectMapperProvider.class);
    }
}
