package com.example.auctionservicesaplication.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//Bid: Reprezentuje ofertę licytacyjną.
@Data
@Entity
@Table(name = "Bids")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bidId")
    private BigDecimal id;

    @ManyToOne()
    @JoinColumn(name = "auctionId")
    private Auction auction;

    @ManyToOne()
    @JoinColumn(name = "userId")
    private User bidder;

    @Column(name = "bidAmount")
    private double bidAmount;

    @Column(name = "bidTime")
    private LocalDateTime bidTime;
}
