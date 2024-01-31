package com.example.auctionservicesaplication.service;

import com.example.auctionservicesaplication.message.AuctionNotFoundException;
import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.repository.AuctionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuctionServiceTest {

    @Mock
    private AuctionRepository auctionRepository;

    @InjectMocks
    private AuctionService auctionService;

    @Test
        // Test sprawdzający, czy metoda getAllAuction() zwraca wszystkie aukcje.
    void getAllAuctions_shouldRetrieveAllAuctions() {
        // Przygotowanie
        List<Auction> expectedAuctions = Collections.singletonList(new Auction());
        when(auctionRepository.findAll()).thenReturn(expectedAuctions);

        // Wywołanie
        List<Auction> actualAuctions = auctionService.getAllAuction();

        // Asercja
        assertEquals(expectedAuctions, actualAuctions, "Powinny zostać pobrane wszystkie aukcje");
    }

    @Test
    // Test sprawdzający, czy metoda getAuctionById() zwraca poprawną aukcję dla danego ID.
    void getAuctionById_withValidId_shouldRetrieveCorrectAuction() {
        // Przygotowanie
        BigDecimal id = BigDecimal.valueOf(1);
        Auction expectedAuction = new Auction();
        when(auctionRepository.findById(id)).thenReturn(Optional.of(expectedAuction));

        // Wywołanie
        Auction actualAuction = auctionService.getAuctionById(id);

        // Asercja
        assertEquals(expectedAuction, actualAuction, "Powinna zostać pobrana poprawna aukcja dla danego ID");
    }

    @Test
    // Test sprawdzający, czy metoda getAuctionById() rzuca wyjątek AuctionNotFoundException dla niepoprawnego ID.
    void getAuctionById_withInvalidId_shouldThrowException() {
        // Przygotowanie
        BigDecimal id = BigDecimal.valueOf(2);
        when(auctionRepository.findById(id)).thenReturn(Optional.empty());

        // Asercja
        assertThrows(AuctionNotFoundException.class, () -> auctionService.getAuctionById(id),
                "Powinien zostać rzucony wyjątek AuctionNotFoundException dla niepoprawnego ID");
    }
}