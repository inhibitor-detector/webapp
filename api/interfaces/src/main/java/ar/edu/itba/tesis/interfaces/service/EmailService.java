package ar.edu.itba.tesis.interfaces.service;

public interface EmailService {
    void sendInhibitionDetectedEmail(String to, String detectorName, String timestamp);
}