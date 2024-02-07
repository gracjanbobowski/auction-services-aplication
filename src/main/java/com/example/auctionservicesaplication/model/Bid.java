package com.example.auctionservicesaplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

// Represents a bid made on an auction.
@Entity
@Table(name = "bids")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    @Min(0)
    @Column(name = "bid_amount")
    private BigDecimal bidAmount;
    @Column(name = "bid_time", nullable = false)
    private LocalDateTime bidTime;

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", auctionId=" + (auction != null ? auction.getId() : "null") +
                ", bidder=" + (bidder != null ? bidder.getUsername() : "null") +
                ", bidAmount=" + bidAmount +
                ", bidTime=" + bidTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return Objects.equals(id, bid.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
