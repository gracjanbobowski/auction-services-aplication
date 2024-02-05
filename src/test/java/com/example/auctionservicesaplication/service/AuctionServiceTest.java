package com.example.auctionservicesaplication.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.example.auctionservicesaplication.message.AuctionNotFoundException;

import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.model.Bid;
import com.example.auctionservicesaplication.model.Category;
import com.example.auctionservicesaplication.model.User;
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

    @Mock
    private BidService bidService;

    @InjectMocks
    private AuctionService auctionService;

    // Test 1: getAllAuctions() - Tests that all auctions are retrieved.
    @Test
    public void getAllAuctions_retrievesAllAuctions() {
        // Arrange
        List<Auction> expectedAuctions = new ArrayList<>();
        expectedAuctions.add(new Auction()); // Add mock Auctions as needed
        when(auctionRepository.findAll()).thenReturn(expectedAuctions);

        // Act
        List<Auction> actualAuctions = auctionService.getAllAuction();

        // Assert
        assertEquals(expectedAuctions, actualAuctions, "The returned auctions should match the expected ones");
    }

    // Test 2: getAuctionById() - Tests retrieving a specific auction by its ID.
    @Test
    void getAuctionById_withValidId_retrievesCorrectAuction() {
        Long id = 1L;
        Auction expectedAuction = new Auction();
        expectedAuction.setId(id);
        when(auctionRepository.findById(id)).thenReturn(Optional.of(expectedAuction));

        Auction actualAuction = auctionService.getAuctionById(id);

        assertEquals(expectedAuction, actualAuction, "The auction should be found with the correct ID");
    }

    // Test 3: getAuctionById() - Tests the response when an invalid ID is used.
    @Test
    void getAuctionById_withInvalidId_throwsException() {
        Long id = 2L;
        when(auctionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(AuctionNotFoundException.class, () -> auctionService.getAuctionById(id),
                "An AuctionNotFoundException should be thrown if the auction is not found");
    }

    // Test 4: createAuction() - Tests creating a new auction with valid data.
    @Test
    void createAuction_withValidData_savesAuction() {
        // Arrange
        Auction newAuction = new Auction();
        newAuction.setTitle("Artwork");
        newAuction.setDescription("Beautiful artwork");
        newAuction.setStartingPrice(new BigDecimal("100.00"));
        newAuction.setCurrentPrice(new BigDecimal("100.00"));
        Category artCategory = new Category();
        artCategory.setCategoryName("Art");
        newAuction.setCategory(artCategory);

        User seller = new User(); // Mock User (seller)
        seller.setUsername("sellerUsername");

        when(auctionRepository.save(any(Auction.class))).thenReturn(newAuction);

        // Act
        Auction savedAuction = auctionService.createAuction(newAuction, seller);

        // Assert
        assertNotNull(savedAuction);
        assertEquals(artCategory, savedAuction.getCategory());
        assertEquals("Artwork", savedAuction.getTitle());
        assertEquals(new BigDecimal("100.00"), savedAuction.getStartingPrice());
        assertEquals(seller, savedAuction.getSeller()); // Verify the seller is set correctly
    }

    // Test 5: createAuction() - Tests creating an auction with a null category.
    @Test
    void createAuction_withNullCategory_throwsException() {
        Auction newAuction = new Auction();
        newAuction.setTitle("Artwork");
        newAuction.setDescription("Beautiful artwork");
        newAuction.setStartingPrice(new BigDecimal("100.00"));
        newAuction.setCurrentPrice(new BigDecimal("100.00"));
        newAuction.setCategory(null);

        User seller = new User(); // Mock User (seller)
        seller.setUsername("sellerUsername");

        assertThrows(IllegalArgumentException.class, () -> auctionService.createAuction(newAuction, seller),
                "Creating an auction with a null category should throw IllegalArgumentException");
    }

/*    // Test 6: editAuction() - Tests editing an auction with valid data.
    @Test
    void editAuction_withValidData_updatesAuction() {
        Long id = 1L;
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
    }*/

    // Test 7: editAuction() - Tests updating the category of an auction.
    @Test
    void editAuction_withNewCategory_updatesCategory() {
        Long id = 1L;
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

    // Test 8: editAuction() - Tests handling an invalid ID during auction edit.
    @Test
    void editAuction_withInvalidId_throwsNotFoundException() {
        Long id = 2L;
        when(auctionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(AuctionNotFoundException.class, () -> auctionService.editAuction(id, new Auction()),
                "An AuctionNotFoundException should be thrown if the auction is not found");
    }

    // Test 9: editAuction() - Tests persistence of unchanged fields during an auction edit.
    @Test
    void editAuction_unchangedFieldsShouldPersist() {
        Long id = 1L;
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

    // Test 10: deleteAuction() - Tests successful deletion of an auction.
    @Test
    void deleteAuction_withValidId_deletesAuction() {
        Long id = 1L;
        Auction auctionToDelete = new Auction();
        auctionToDelete.setId(id);

        when(auctionRepository.findById(id)).thenReturn(Optional.of(auctionToDelete));

        auctionService.deleteAuction(id);

        verify(auctionRepository).delete(auctionToDelete);
    }

    // Test 11: deleteAuction() - Tests handling an invalid ID during auction deletion.
    @Test
    void deleteAuction_withInvalidId_throwsNotFoundException() {
        Long id = 2L;

        when(auctionRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AuctionNotFoundException.class, () -> {
            auctionService.deleteAuction(id);
        });

        String expectedMessage = "Auction not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // Test 12: addBidToAuction() - Tests adding a bid to an auction.
    @Test
    void addBidToAuction_addsBidToAuction() {
        Auction auction = new Auction();
        auction.setId(1L); // Ensure the auction has an ID
        auction.setBids(new HashSet<>()); // Initialize the bids set

        Bid bid = new Bid();
        bid.setBidAmount(new BigDecimal("100.00")); // Corrected to BigDecimal
        bid.setBidTime(LocalDateTime.now());

        auctionService.addBidToAuction(auction, bid);

        assertTrue(auction.getBids().contains(bid), "The bid should be added to the auction's bids set");
        verify(auctionRepository).save(auction);
    }

/*    // Test 13: getHighestBidAmount() - Tests getting the highest bid amount for a given auction.
    @Test
    public void getHighestBidAmount_withValidAuction_returnsHighestAmount() {
        // Arrange
        Auction auction = new Auction();
        auction.setId(1L);
        Set<Bid> bids = new HashSet<>();
        bids.add(new Bid(null, auction, null, new BigDecimal("100.00"), LocalDateTime.now()));
        bids.add(new Bid(null, auction, null, new BigDecimal("200.00"), LocalDateTime.now()));
        auction.setBids(bids); // Setting the bids to the auction

        // Act
        BigDecimal highestBidAmount = auctionService.getHighestBidAmount(auction);

        // Assert
        assertEquals(new BigDecimal("200.00"), highestBidAmount, "The highest bid amount should be returned.");
    }*/

    // Test 14: updateAuction() - Tests updating an auction.
    @Test
    public void updateAuction_withValidAuction_updatesAuction() {
        // Arrange
        Auction auction = new Auction();
        auction.setId(1L);
        auction.setTitle("Updated Auction");

        // Act
        auctionService.updateAuction(auction);

        // Assert
        verify(auctionRepository).save(auction);
    }

    // Test 15: updateAuctionWithBid() - Tests updating an auction with a new bid.
    @Test
    public void updateAuctionWithBid_withValidBid_updatesCurrentPrice() {
        // Arrange
        Auction auction = new Auction();
        auction.setId(1L);
        auction.setCurrentPrice(new BigDecimal("100.00")); // Set an initial current price
        Bid newBid = new Bid(null, auction, null, new BigDecimal("300.00"), LocalDateTime.now());

        // Act
        auctionService.updateAuctionWithBid(auction, newBid);

        // Assert
        assertEquals(new BigDecimal("300.00"), auction.getCurrentPrice(), "The current price should be updated to the new bid amount.");
        verify(auctionRepository).save(auction);
    }
}
