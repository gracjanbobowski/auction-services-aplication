package com.example.auctionservicesaplication.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.example.auctionservicesaplication.message.AuctionNotFoundException;

import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.model.Bid;
import com.example.auctionservicesaplication.model.Category;
import com.example.auctionservicesaplication.repository.AuctionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class AuctionServiceTest {

    @Mock
    private AuctionRepository auctionRepository;

    @InjectMocks
    private AuctionService auctionService;

    @Test
    public void whenGetAllAuctionCalled_thenRetrieveAuctions() {
        // Arrange
        List<Auction> expectedAuctions = new ArrayList<>();
        expectedAuctions.add(new Auction()); // Add mock Auctions as needed
        when(auctionRepository.findAll()).thenReturn(expectedAuctions);

        // Act
        List<Auction> actualAuctions = auctionService.getAllAuction();

        // Assert
        assertEquals(expectedAuctions, actualAuctions, "The returned auctions should match the expected ones");
    }

    @Test
    void whenValidId_thenAuctionShouldBeFound() {
        BigDecimal id = BigDecimal.valueOf(1);
        Auction expectedAuction = new Auction();
        expectedAuction.setId(id);
        when(auctionRepository.findById(id)).thenReturn(Optional.of(expectedAuction));

        Auction actualAuction = auctionService.getAuctionById(id);

        assertEquals(expectedAuction, actualAuction, "The auction should be found with the correct ID");
    }

    @Test
    void whenInvalidId_thenThrowException() {
        BigDecimal id = BigDecimal.valueOf(2);
        when(auctionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(AuctionNotFoundException.class, () -> auctionService.getAuctionById(id),
                "An AuctionNotFoundException should be thrown if the auction is not found");
    }

    @Test
    void whenCreateAuctionWithValidData_thenAuctionShouldBeSaved() {
        // Arrange
        Auction newAuction = new Auction();
        newAuction.setTitle("Artwork");
        newAuction.setDescription("Beautiful artwork");
        newAuction.setStartingPrice(new BigDecimal("100.00"));
        newAuction.setCurrentPrice(new BigDecimal("100.00"));
        Category artCategory = new Category();
        artCategory.setCategoryName("Art");
        newAuction.setCategory(artCategory);
        when(auctionRepository.save(any(Auction.class))).thenReturn(newAuction);

        // Act
        Auction savedAuction = auctionService.createAuction(newAuction);

        // Assert
        assertNotNull(savedAuction);
        assertEquals(artCategory, savedAuction.getCategory());
        assertEquals("Artwork", savedAuction.getTitle());
        assertEquals(new BigDecimal("100.00"), savedAuction.getStartingPrice());
    }

    @Test
    void whenCreateAuctionWithNullCategory_thenThrowException() {
        // Arrange
        Auction newAuction = new Auction();
        newAuction.setTitle("Artwork");
        newAuction.setDescription("Beautiful artwork");
        newAuction.setStartingPrice(new BigDecimal("100.00"));
        newAuction.setCurrentPrice(new BigDecimal("100.00"));
        newAuction.setCategory(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> auctionService.createAuction(newAuction),
                "Creating an auction with a null category should throw IllegalArgumentException");
    }

    @Test
    void whenValidId_thenAuctionShouldBeUpdated() {
        BigDecimal id = new BigDecimal("1");
        Auction existingAuction = new Auction();
        existingAuction.setId(id);

        Auction editedAuction = new Auction();
        editedAuction.setTitle("Updated Title");
        editedAuction.setDescription("Updated Description");
        editedAuction.setStartingPrice(new BigDecimal("150.00"));
        editedAuction.setCurrentPrice(new BigDecimal("200.00"));
        editedAuction.setStartTime(LocalDateTime.now());
        editedAuction.setEndTime(LocalDateTime.now().plusDays(1));

        when(auctionRepository.findById(id)).thenReturn(Optional.of(existingAuction));
        when(auctionRepository.save(any(Auction.class))).thenReturn(existingAuction);

        auctionService.editAuction(id, editedAuction);

        ArgumentCaptor<Auction> auctionCaptor = ArgumentCaptor.forClass(Auction.class);
        verify(auctionRepository).save(auctionCaptor.capture());
        Auction savedAuction = auctionCaptor.getValue();

        assertNotNull(savedAuction);
        assertEquals(id, savedAuction.getId());
        assertEquals("Updated Title", savedAuction.getTitle());
        assertEquals("Updated Description", savedAuction.getDescription());
        assertEquals(new BigDecimal("150.00"), savedAuction.getStartingPrice());
        assertEquals(new BigDecimal("200.00"), savedAuction.getCurrentPrice());
        assertNotNull(savedAuction.getStartTime());
        assertNotNull(savedAuction.getEndTime());
    }


    @Test
    void whenValidIdAndNewCategory_thenAuctionCategoryShouldBeUpdated() {
        BigDecimal id = new BigDecimal("1");
        Auction existingAuction = new Auction();
        existingAuction.setId(id);

        Category oldCategory = new Category();
        oldCategory.setCategoryName("Old Category");
        existingAuction.setCategory(oldCategory);

        Auction editedAuction = new Auction();
        Category newCategory = new Category();
        newCategory.setCategoryName("New Category");
        editedAuction.setCategory(newCategory);

        when(auctionRepository.findById(id)).thenReturn(Optional.of(existingAuction));
        when(auctionRepository.save(any(Auction.class))).thenReturn(existingAuction);

        auctionService.editAuction(id, editedAuction);

        ArgumentCaptor<Auction> auctionCaptor = ArgumentCaptor.forClass(Auction.class);
        verify(auctionRepository).save(auctionCaptor.capture());
        Auction savedAuction = auctionCaptor.getValue();

        assertNotNull(savedAuction.getCategory());
        assertEquals("New Category", savedAuction.getCategory().getCategoryName());
    }

    @Test
    void whenInvalidId_thenThrowExceptionOnEdit() {
        BigDecimal id = new BigDecimal("2");
        when(auctionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(AuctionNotFoundException.class, () -> auctionService.editAuction(id, new Auction()),
                "An AuctionNotFoundException should be thrown if the auction is not found");
    }

    @Test
    void whenAuctionIsEdited_unchangedFieldsShouldPersist() {
        BigDecimal id = new BigDecimal("1");
        Auction existingAuction = new Auction();
        existingAuction.setId(id);
        existingAuction.setTitle("Original Title");
        existingAuction.setDescription("Original Description");
        existingAuction.setStartingPrice(new BigDecimal("100.00"));
        Set<Bid> originalBids = new HashSet<>();
        existingAuction.setBids(originalBids);

        Auction editedAuction = new Auction();
        editedAuction.setDescription("Updated Description");

        when(auctionRepository.findById(id)).thenReturn(Optional.of(existingAuction));
        when(auctionRepository.save(any(Auction.class))).thenReturn(existingAuction);

        auctionService.editAuction(id, editedAuction);

        ArgumentCaptor<Auction> auctionCaptor = ArgumentCaptor.forClass(Auction.class);
        verify(auctionRepository).save(auctionCaptor.capture());
        Auction savedAuction = auctionCaptor.getValue();

        assertEquals("Original Title", savedAuction.getTitle());  // Title should not change
        assertEquals("Updated Description", savedAuction.getDescription());
        assertEquals(originalBids, savedAuction.getBids());  // Bids should remain the same
    }


}
