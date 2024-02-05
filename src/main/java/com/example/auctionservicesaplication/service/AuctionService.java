package com.example.auctionservicesaplication.service;

import com.example.auctionservicesaplication.message.AuctionNotFoundException;
import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.model.Bid;
import com.example.auctionservicesaplication.model.User;
import com.example.auctionservicesaplication.repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Service class for business logic related to auctions.
@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final BidService bidService;

    // Constructor for dependency injection of repositories and services.
    @Autowired
    public AuctionService(AuctionRepository auctionRepository, BidService bidService) {
        this.auctionRepository = auctionRepository;
        this.bidService = bidService;
    }

    // Retrieves all auctions from the repository.
    public List<Auction> getAllAuction() {
        return auctionRepository.findAll();
    }

    // Retrieves an auction by its ID or throws an exception if not found.
    public Auction getAuctionById(Long auctionId) {
        return auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found"));
    }

    public List<Auction> getAuctionsBySeller(User seller) {
        return auctionRepository.findBySeller(seller);
    }

    // Creates a new auction and assigns the seller.
    public Auction createAuction(Auction auction, User seller) {
        if (auction.getCategory() == null) {
            throw new IllegalArgumentException("Category of the auction cannot be empty.");
        }

        auction.setSeller(seller);
        auction.setCurrentPrice(auction.getStartingPrice());
        return auctionRepository.save(auction);
    }

    // Retrieves the highest bid amount for a specific auction
    public BigDecimal getHighestBidAmount(Auction auction) {
        return auction.getBids().stream()
                .map(Bid::getBidAmount)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO); // Return 0 if no bids are present
    }

    // Updates an existing auction.
    public void updateAuction(Auction auction) {
        auctionRepository.save(auction);
    }

    // Edits an auction by updating its details.
    public void editAuction(Long auctionId, Auction editedAuction) {
        Auction existingAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found"));

        // Update fields if they are not null in the editedAuction
        if (editedAuction.getCategory() != null) {
            existingAuction.setCategory(editedAuction.getCategory());
        }
        if (editedAuction.getTitle() != null) {
            existingAuction.setTitle(editedAuction.getTitle());
        }
        if (editedAuction.getDescription() != null) {
            existingAuction.setDescription(editedAuction.getDescription());
        }
        if (editedAuction.getStartTime() != null) {
            existingAuction.setStartTime(editedAuction.getStartTime());
        }
        if (editedAuction.getEndTime() != null) {
            existingAuction.setEndTime(editedAuction.getEndTime());
        }

        // Check if there are any bids
        if (existingAuction.getBids() == null || existingAuction.getBids().isEmpty()) {
            // If no bids, set both starting and current price to edited starting price or retain existing if not edited
            BigDecimal newStartingPrice = editedAuction.getStartingPrice() != null ? editedAuction.getStartingPrice() : existingAuction.getStartingPrice();
            existingAuction.setStartingPrice(newStartingPrice);
            existingAuction.setCurrentPrice(newStartingPrice);
        } else {
            // If there are bids, apply the specified logic
            if (editedAuction.getStartingPrice() != null) {
                existingAuction.setStartingPrice(editedAuction.getStartingPrice());
                BigDecimal highestBid = getHighestBidAmount(existingAuction);
                BigDecimal newCurrentPrice = highestBid.compareTo(existingAuction.getStartingPrice()) > 0 ? highestBid : existingAuction.getStartingPrice();
                existingAuction.setCurrentPrice(newCurrentPrice);
            }
        }

        auctionRepository.save(existingAuction);
    }


    // Deletes an auction by its ID.
    public void deleteAuction(Long auctionId) {
        Auction auctionToDelete = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found"));
        auctionRepository.delete(auctionToDelete);
    }

    // Updates the auction with a new bid and saves it.
    public void updateAuctionWithBid(Auction auction, Bid newBid) {
        // Initialize current price if null
        if (auction.getCurrentPrice() == null) {
            auction.setCurrentPrice(BigDecimal.ZERO);
        }

        if (newBid.getBidAmount().compareTo(auction.getCurrentPrice()) > 0) {
            auction.setCurrentPrice(newBid.getBidAmount());
        }
        auctionRepository.save(auction);
    }

    // Adds a new bid to the auction and updates the auction.
    public void addBidToAuctionAndUpdate(Auction auction, Bid newBid) {
        addBidToAuction(auction, newBid);
        updateAuctionWithBid(auction, newBid);
    }

    // Adds a new bid to the auction.
    public void addBidToAuction(Auction auction, Bid newBid) {
        Set<Bid> bids = auction.getBids();
        if (bids == null) {
            bids = new HashSet<>();
            auction.setBids(bids);
        }
        bids.add(newBid);
        auctionRepository.save(auction);
    }
}
