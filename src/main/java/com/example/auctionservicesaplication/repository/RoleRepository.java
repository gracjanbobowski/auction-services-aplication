package com.example.auctionservicesaplication.repository;

import com.example.auctionservicesaplication.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository for managing Role entities in the database.

// The JpaRepository interface from Spring Data provides basic CRUD operations (Create, Read, Update, Delete).
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    // Method to find a role by its name.
    Role findByName(String name);
}
