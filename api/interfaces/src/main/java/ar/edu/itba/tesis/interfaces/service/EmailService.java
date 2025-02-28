package ar.edu.itba.tesis.interfaces.service;

import org.springframework.scheduling.annotation.Async;

public interface EmailService {
    @Async
    void sendInhibitionDetectedEmail(String to, String detectorName, String timestamp);
}