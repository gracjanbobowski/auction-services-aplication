package com.example.auctionservicesaplication.repository;

import com.example.auctionservicesaplication.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

//AuctionRepository: Repozytorium do zarządzania encjami aukcji w bazie danych.
@Repository
public interface AuctionRepository extends JpaRepository<Auction, BigDecimal> {
    // Dodatkowe metody związane z bazą danych, jeśli są potrzebne
}
