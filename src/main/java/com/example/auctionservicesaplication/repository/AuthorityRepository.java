package com.example.auctionservicesaplication.repository;

import com.example.auctionservicesaplication.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository for managing Authority entities in the database.

// The JpaRepository interface from Spring Data provides basic CRUD operations (Create, Read, Update, Delete).
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}