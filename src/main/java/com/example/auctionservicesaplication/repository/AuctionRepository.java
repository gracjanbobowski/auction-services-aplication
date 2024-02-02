package com.example.auctionservicesaplication.repository;

import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

// Repository for managing Auction entities in the database.

// The JpaRepository interface from Spring Data provides basic CRUD operations (Create, Read, Update, Delete).
@Repository
public interface AuctionRepository extends JpaRepository<Auction, BigDecimal> {
    // Method to find auctions by the seller (user).
    List<Auction> findBySeller(User seller);
}
