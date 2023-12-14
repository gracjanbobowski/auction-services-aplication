package com.example.auctionservicesaplication.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//User: Reprezentuje użytkownika.

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private BigDecimal id;

    @OneToMany(mappedBy = "seller")
    private List<Auction> auctions;

    @OneToMany(mappedBy = "bidder")
    private List<Bid> bids;

    @OneToMany(mappedBy = "reviewer")
    private List<TransactionRating> transactionRatings;

    private String username;
    private String email;
    private String password;

}
