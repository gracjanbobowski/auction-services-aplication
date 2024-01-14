package com.example.auctionservicesaplication.repository;

import com.example.auctionservicesaplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


//UserRepository: Repozytorium do zarządzania użytkownikami w bazie danych.
@Repository
public interface UserRepository extends JpaRepository<User, BigDecimal> {
    @Query("SELECT u FROM user u WHERE u.username = :username")
    public User getUserByUsername(@Param("username") String username);

    // Dodatkowe metody związane z bazą danych, jeśli są potrzebne
}
