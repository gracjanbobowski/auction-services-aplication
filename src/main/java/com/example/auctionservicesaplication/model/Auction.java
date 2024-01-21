package com.example.auctionservicesaplication.model;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

//Auction: Reprezentuje aukcję.

@Data
@Entity
@Table(name = "auctions")
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auction_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id")  // adjust the column name accordingly
    private User seller;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "Kategoria aukcji nie może być pusta.")
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(name = "starting_price", columnDefinition = "DECIMAL(10, 2) DEFAULT 0.0", nullable = false)
    private BigDecimal startingPrice;

    @Column(name = "current_price", nullable = false, columnDefinition = "DECIMAL(10, 2) default 0.0")
    private BigDecimal currentPrice;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Bid> bids;

    public Auction() {
        this.currentPrice = BigDecimal.ZERO;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Set<Bid> getBids() {
        return bids;
    }
}