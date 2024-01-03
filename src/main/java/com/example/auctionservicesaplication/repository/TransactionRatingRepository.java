package com.example.auctionservicesaplication.repository;

import com.example.auctionservicesaplication.model.TransactionRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


//TransactionRatingRepository: Repozytorium do zarządzania ocenami transakcji w bazie danych.
@Repository
public interface TransactionRatingRepository extends JpaRepository<TransactionRating, BigDecimal> {
    // Dodatkowe metody związane z bazą danych, jeśli są potrzebne
}
