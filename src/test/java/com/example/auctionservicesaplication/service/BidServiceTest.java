package com.example.auctionservicesaplication.service;

import com.example.auctionservicesaplication.model.*;
import com.example.auctionservicesaplication.message.*;
import com.example.auctionservicesaplication.repository.BidRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BidServiceTest {

    @Mock
    private BidRepository bidRepository;

    @InjectMocks
    private BidService bidService;

    // Test 1: getAllBids() - Tests that all bids are retrieved.
    @Test
    public void getAllBids_retrievesAllBids() {
        // Arrange
        List<Bid> expectedBids = Arrays.asList(
                new Bid(1L, new Auction(), new User(), 100.00, LocalDateTime.now()),
                new Bid(2L, new Auction(), new User(), 200.00, LocalDateTime.now().plusDays(1))
        );
        when(bidRepository.findAll()).thenReturn(expectedBids);

        // Act
        List<Bid> actualBids = bidService.getAllBids();

        // Assert
        assertNotNull(actualBids, "The list of bids should not be null");
        assertEquals(expectedBids, actualBids, "The returned bids should match the expected list of bids");
    }

    // Test 2: getBidById() - Tests retrieving a specific bid by its ID.
    @Test
    void getBidById_withValidId_retrievesCorrectBid() {
        // Arrange
        BigDecimal bidId = BigDecimal.ONE;
        Bid expectedBid = new Bid(1L, new Auction(), new User(), 100.00, LocalDateTime.now());
        when(bidRepository.findById(bidId)).thenReturn(Optional.of(expectedBid));

        // Act
        Bid actualBid = bidService.getBidById(bidId);

        // Assert
        assertEquals(expectedBid, actualBid, "The bid should be retrieved with the correct ID");
    }

    // Test 3: getBidById() - Tests retrieving a bid with an ID that does not exist.
    @Test
    void getBidById_withInvalidId_throwsException() {
        // Arrange
        BigDecimal bidId = BigDecimal.ONE;
        when(bidRepository.findById(bidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BidNotFoundException.class, () -> bidService.getBidById(bidId),
                "A BidNotFoundException should be thrown if the bid is not found");
    }

    // Test 4: createBid() - Tests that a bid is saved correctly.
    @Test
    void createBid_savesBidCorrectly() {
        // Arrange
        Bid newBid = new Bid();
        newBid.setBidAmount(150.00);
        LocalDateTime fixedTime = LocalDateTime.of(2024, 1, 26, 1, 4, 25, 773120000);
        newBid.setBidTime(fixedTime);

        // You don't need to set the ID here as you're not expecting the method to return anything

        // Act
        bidService.createBid(newBid);

        // Assert
        ArgumentCaptor<Bid> bidArgumentCaptor = ArgumentCaptor.forClass(Bid.class);
        verify(bidRepository).save(bidArgumentCaptor.capture());
        Bid capturedBid = bidArgumentCaptor.getValue();

        assertEquals(150.00, capturedBid.getBidAmount(), "The bid amount should be as expected.");
        assertEquals(fixedTime, capturedBid.getBidTime(), "The bid time should be as expected.");
    }


    // Test 5: editBid() - Tests that an existing bid is updated correctly.
    @Test
    void editBid_updatesBidCorrectly() {
        // Arrange
        BigDecimal bidId = BigDecimal.ONE;
        LocalDateTime initialTime = LocalDateTime.of(2024, 1, 25, 1, 4, 25);
        Bid existingBid = new Bid(1L, new Auction(), new User(), 100.00, initialTime);

        LocalDateTime newTime = LocalDateTime.of(2024, 1, 26, 1, 4, 25);
        Bid editedBid = new Bid(1L, new Auction(), new User(), 200.00, newTime);

        when(bidRepository.findById(bidId)).thenReturn(Optional.of(existingBid));
        when(bidRepository.save(any(Bid.class))).thenReturn(editedBid);

        // Act
        bidService.editBid(bidId, editedBid);

        // Assert
        ArgumentCaptor<Bid> bidArgumentCaptor = ArgumentCaptor.forClass(Bid.class);
        verify(bidRepository).save(bidArgumentCaptor.capture());
        Bid capturedBid = bidArgumentCaptor.getValue();

        assertEquals(200.00, capturedBid.getBidAmount(), "The bid amount should be updated to the new value.");
        assertEquals(newTime, capturedBid.getBidTime(), "The bid time should be updated to the new value.");
    }
    // Test 6: editBid() - Tests that a non-existent bid throws an exception when attempting to update.
    @Test
    void editBid_withNonExistentBid_throwsException() {
        // Arrange
        BigDecimal bidId = BigDecimal.ONE;
        when(bidRepository.findById(bidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BidNotFoundException.class, () -> bidService.editBid(bidId, new Bid()),
                "A BidNotFoundException should be thrown if the bid to be edited is not found.");
    }
    // Test 7: deleteBid() - Tests that a bid is deleted correctly.
    @Test
    void deleteBid_deletesBidCorrectly() {
        // Arrange
        BigDecimal bidId = BigDecimal.ONE;
        Bid bidToDelete = new Bid(1L, new Auction(), new User(), 100.00, LocalDateTime.now());

        when(bidRepository.findById(bidId)).thenReturn(Optional.of(bidToDelete));

        // Act
        bidService.deleteBid(bidId);

        // Assert
        verify(bidRepository).delete(bidToDelete);
    }
    // Test 8: deleteBid() - Tests that attempting to delete a non-existent bid throws an exception.
    @Test
    void deleteBid_withNonExistentBid_throwsException() {
        // Arrange
        BigDecimal bidId = BigDecimal.ONE;
        when(bidRepository.findById(bidId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BidNotFoundException.class, () -> bidService.deleteBid(bidId),
                "A BidNotFoundException should be thrown if the bid to be deleted is not found.");
    }
    // Test 9: getBidsByAuction() - Tests retrieving all bids for a specific auction.
    @Test
    void getBidsByAuction_retrievesBidsForGivenAuction() {
        // Arrange
        Auction auction = new Auction();
        auction.setId(new BigDecimal("1")); // Assuming the auction has an ID of 1

        Bid bid1 = new Bid();
        bid1.setId(1L);
        bid1.setAuction(auction);

        Bid bid2 = new Bid();
        bid2.setId(2L);
        bid2.setAuction(auction);

        List<Bid> expectedBids = List.of(bid1, bid2);
        when(bidRepository.findByAuction(auction)).thenReturn(expectedBids);

        // Act
        List<Bid> actualBids = bidService.getBidsByAuction(auction);

        // Assert
        assertEquals(expectedBids, actualBids, "The retrieved bids should match the expected list for the auction");
        verify(bidRepository).findByAuction(auction); // Verifies that the repository method was called with the correct auction
    }

}
