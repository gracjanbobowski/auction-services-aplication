package com.example.auctionservicesaplication.repository;

import com.example.auctionservicesaplication.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


//BidRepository: Repozytorium do zarządzania ofertami licytacyjnymi w bazie danych.
@Repository
public interface BidRepository extends JpaRepository<Bid, BigDecimal> {
    // Dodatkowe metody związane z bazą danych, jeśli są potrzebne
}
