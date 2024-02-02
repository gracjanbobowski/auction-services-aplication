package com.example.auctionservicesaplication.repository;

import com.example.auctionservicesaplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Optional;

// Repository for managing User entities in the database.

// The JpaRepository interface from Spring Data provides basic CRUD operations (Create, Read, Update, Delete).
@Repository
public interface UserRepository extends JpaRepository<User, BigDecimal> {

    // Method to find a user by their username. Returns an Optional.
    Optional<User> findByUsername(String username);
}
