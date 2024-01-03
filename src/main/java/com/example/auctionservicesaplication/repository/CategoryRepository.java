package com.example.auctionservicesaplication.repository;

import com.example.auctionservicesaplication.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


//CategoryRepository: Repozytorium do zarządzania kategoriami w bazie danych.
@Repository
public interface CategoryRepository extends JpaRepository<Category, BigDecimal> {
    // Dodatkowe metody związane z bazą danych, jeśli są potrzebne
}