package com.example.auctionservicesaplication.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//TransactionRating: Reprezentuje ocenę transakcji.

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction_ratings")
public class TransactionRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_rating_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User reviewer;

    private LocalDateTime ratingTime;
    private int rating;
    private String comment;
}
