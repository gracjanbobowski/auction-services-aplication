package com.example.auctionservicesaplication.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    // Test 1: sendRegistrationConfirmation() - Verifies that a registration confirmation email is sent with the correct content.
    @Test
    public void sendRegistrationConfirmation_sendsCorrectEmail() {
        // Arrange
        String to = "user@example.com";
        String username = "testUser";

        // Act
        emailService.sendRegistrationConfirmation(to, username);

        // Assert
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();

        assertEquals(to, sentMessage.getTo()[0]);
        assertEquals("Potwierdzenie rejestracji w serwisie aukcyjnym", sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains("Witaj testUser!"));
        assertTrue(sentMessage.getText().contains("Dziękujemy za rejestrację w naszym serwisie aukcyjnym."));
    }

    // Test 2: sendEmail() - Verifies that an email is sent with the correct content.
    @Test
    public void sendEmail_sendsCorrectEmail() {
        // Arrange
        String to = "user@example.com";
        String subject = "Test Subject";
        String body = "Test body of the email";

        // Act
        emailService.sendEmail(to, subject, body);

        // Assert
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();

        assertEquals(to, sentMessage.getTo()[0]);
        assertEquals(subject, sentMessage.getSubject());
        assertEquals(body, sentMessage.getText());
    }
}
