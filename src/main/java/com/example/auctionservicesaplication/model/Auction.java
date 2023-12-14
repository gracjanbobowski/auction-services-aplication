package com.example.auctionservicesaplication.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//Auction: Reprezentuje aukcję.

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "auctions")
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auction_id")
    private BigDecimal id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String title;
    private String description;
    private double startingPrice;
    private double currentPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
