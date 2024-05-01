package ar.edu.itba.tesis.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 */
@SpringBootApplication(scanBasePackages = {
        "ar.edu.itba.tesis.webapp",
        "ar.edu.itba.tesis.services",
        "ar.edu.itba.tesis.interfaces",
        "ar.edu.itba.tesis.models",
        "ar.edu.itba.tesis.persistence"
})
@EntityScan(basePackages = {
        "ar.edu.itba.tesis.models"
})
@EnableTransactionManagement
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
