package com.example.auctionservicesaplication.repository;

import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

//AuctionRepository: Repozytorium do zarządzania encjami aukcji w bazie danych.
@Repository
public interface AuctionRepository extends JpaRepository<Auction, BigDecimal> {
    List<Auction> findBySeller(User seller);
}
