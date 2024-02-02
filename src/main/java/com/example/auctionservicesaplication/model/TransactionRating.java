package com.example.auctionservicesaplication.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Represents a transaction rating in the system.
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

    // Many-to-one relationship with User. A user can give multiple ratings.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User reviewer;

    // Fields for the time of the rating, the rating value, and any additional comments.
    private LocalDateTime ratingTime;
    private int rating;
    private String comment;
}
