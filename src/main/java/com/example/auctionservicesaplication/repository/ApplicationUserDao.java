package com.example.auctionservicesaplication.repository;

import com.example.auctionservicesaplication.auth.ApplicationUser;

import java.util.Optional;

public interface ApplicationUserDao {
    Optional<ApplicationUser> selectApplicationUserByUsername(String username);
}
