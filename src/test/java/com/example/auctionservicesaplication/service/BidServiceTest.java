package com.example.auctionservicesaplication.service;

import com.example.auctionservicesaplication.model.*;
import com.example.auctionservicesaplication.repository.BidRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BidServiceTest {

    @Mock
    private BidRepository bidRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private BidService bidService;

    @Test
        // Test sprawdza czy oferta jest zapisywana poprawnie.
    void createBid_savesBidCorrectly() {
        // Arrange
        Bid newBid = new Bid();
        newBid.setBidAmount(new BigDecimal("150.00"));
        LocalDateTime fixedTime = LocalDateTime.now();
        newBid.setBidTime(fixedTime);

        // Act
        bidService.createBid(newBid);

        // Assert
        ArgumentCaptor<Bid> bidArgumentCaptor = ArgumentCaptor.forClass(Bid.class);
        verify(bidRepository).save(bidArgumentCaptor.capture());
        Bid capturedBid = bidArgumentCaptor.getValue();

        assertEquals(new BigDecimal("150.00"), capturedBid.getBidAmount(), "The bid amount should be as expected.");
        assertEquals(fixedTime, capturedBid.getBidTime(), "The bid time should be as expected.");
    }

    @Test
    // Test sprawdza, czy potwierdzenie oferty jest wysyłane poprawnie.
    void sendBidConfirmation_sendsEmailCorrectly() {
        // Arrange
        String toEmail = "user@example.com";
        String username = "testUser";
        String auctionTitle = "Artwork Auction";

        // Act
        bidService.sendBidConfirmation(toEmail, username, auctionTitle);

        // Assert
        String expectedSubject = "Potwierdzenie licytacji";
        String expectedBody = "Cześć " + username + "!\nDziękujemy za złożenie oferty licytacyjnej na aukcję o tytule: " + auctionTitle + ".\nŻyczymy powodzenia!";

        ArgumentCaptor<String> toCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);

        verify(emailService).sendEmail(toCaptor.capture(), subjectCaptor.capture(), bodyCaptor.capture());

        assertEquals(toEmail, toCaptor.getValue(), "Email should be sent to the correct recipient.");
        assertEquals(expectedSubject, subjectCaptor.getValue(), "Email subject should be as expected.");
        assertEquals(expectedBody, bodyCaptor.getValue(), "Email body should be as expected.");
    }
}