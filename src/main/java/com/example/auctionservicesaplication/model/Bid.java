package com.example.auctionservicesaplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Represents a bid made on an auction.
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bids")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_id")
    private Long id;

    // Many-to-one relationship with Auction. Each bid is associated with one auction.
    @ManyToOne
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    // Many-to-one relationship with User. Each bid is made by one user.
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User bidder;

    // Fields for the bid amount and the time when the bid was made.
    @Column(name = "bid_amount")
    private BigDecimal bidAmount;
    @Column(name = "bidTime", nullable = false)
    private LocalDateTime bidTime;
}
