package ar.edu.itba.tesis.services;

import ar.edu.itba.tesis.interfaces.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    
    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendInhibitionDetectedEmail(String to, String detectorName, String timestamp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Inhibition Detected - " + detectorName);
        message.setText("An inhibition signal was detected by detector '" + detectorName + "' at " + timestamp);
        try {
            emailSender.send(message);
        } catch (MailException e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }
}