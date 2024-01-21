package com.example.auctionservicesaplication.repository;

import com.example.auctionservicesaplication.model.Auction;
import com.example.auctionservicesaplication.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


//BidRepository: Repozytorium do zarządzania ofertami licytacyjnymi w bazie danych.
@Repository
public interface BidRepository extends JpaRepository<Bid, BigDecimal> {
    List<Bid> findByAuction(Auction auction);
}
