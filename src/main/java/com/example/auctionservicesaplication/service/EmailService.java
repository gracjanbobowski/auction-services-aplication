package com.example.auctionservicesaplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


//UserService: Logika biznesowa związana z użytkownikami.
@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendRegistrationConfirmation(String to, String username) {
        String subject = "Potwierdzenie rejestracji w serwisie aukcyjnym";
        String body = "Witaj " + username + "!\n" +
                "Dziękujemy za rejestrację w naszym serwisie aukcyjnym.\n" +
                "Życzymy udanych zakupów i sprzedaży!";
        sendEmail(to, subject, body);
    }

    public void sendBidConfirmation(String to, String username, String auctionTitle) {
        String subject = "Potwierdzenie licytacji";
        String body = "Cześć " + username + "!\n" +
                "Dziękujemy za złożenie oferty licytacyjnej na aukcję o tytule: " + auctionTitle + ".\n" +
                "Życzymy powodzenia!";
        sendEmail(to, subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }
}

