package com.example.auctionservicesaplication.repository;

import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

// Repository for managing Bid entities in the database.

// The JpaRepository interface from Spring Data provides basic CRUD operations (Create, Read, Update, Delete).
@Repository
public interface BidRepository extends JpaRepository<Bid, BigDecimal> {
    // Method to find bids associated with a specific auction.
    List<Bid> findByAuction(Auction auction);

    // Override the save method to handle Bid entities.
    @Override
    <S extends Bid> S save(S entity);
}
