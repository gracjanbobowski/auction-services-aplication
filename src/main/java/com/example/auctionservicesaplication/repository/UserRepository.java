package com.example.auctionservicesaplication.repository;

import com.example.auctionservicesaplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Optional;

//UserRepository: Repozytorium do zarządzania użytkownikami w bazie danych.
@Repository
public interface UserRepository extends JpaRepository<User, BigDecimal> {

    Optional<User> findByUsername(String username);
}
