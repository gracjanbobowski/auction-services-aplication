package com.example.auctionservicesaplication.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//TransactionRating: Reprezentuje ocenę transakcji.

@Data
@Entity
@Table(name = "TransactionRatings")
public class TransactionRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionRatingId")
    private BigDecimal id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User reviewer;

    @Column(name = "ratingTime")
    private LocalDateTime ratingTime;

    @Column(name = "rating")
    private int rating;

    @Column(name = "comment")
    private String comment;
}
