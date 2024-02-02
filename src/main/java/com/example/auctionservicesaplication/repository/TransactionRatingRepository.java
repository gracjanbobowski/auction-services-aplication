package com.example.auctionservicesaplication.repository;

import com.example.auctionservicesaplication.model.TransactionRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

// Repository for managing TransactionRating entities in the database.

// The JpaRepository interface from Spring Data provides basic CRUD operations (Create, Read, Update, Delete).
@Repository
public interface TransactionRatingRepository extends JpaRepository<TransactionRating, BigDecimal> {

}
