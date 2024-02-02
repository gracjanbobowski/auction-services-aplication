package com.example.auctionservicesaplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// Service class for handling email notifications (e.g., registration confirmations).
@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    // Constructor for dependency injection of the JavaMailSender.
    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    // Sends a registration confirmation email.
    public void sendRegistrationConfirmation(String to, String username) {
        String subject = "Potwierdzenie rejestracji w serwisie aukcyjnym";
        String body = "Witaj " + username + "!\n" +
                "Dziękujemy za rejestrację w naszym serwisie aukcyjnym.\n" +
                "Życzymy udanych zakupów i sprzedaży!";
        sendEmail(to, subject, body);
    }

    // Method to send an email with the given parameters.
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }
}
