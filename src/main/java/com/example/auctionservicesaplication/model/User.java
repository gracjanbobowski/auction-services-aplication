package com.example.auctionservicesaplication.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;
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
    private Long id;

    @OneToMany(mappedBy = "seller")
    private List<Auction> auctions;

    @OneToMany(mappedBy = "bidder")
    private List<Bid> bids;

    @OneToMany(mappedBy = "reviewer")
    private List<TransactionRating> transactionRatings;

    @NotEmpty(message = "Username is required")
    private String username;

    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;

}
