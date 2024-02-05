package com.example.auctionservicesaplication.model;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

// Represents an auction entity in the application.
@Entity
@Table(name = "auctions")
@Getter @Setter
@NoArgsConstructor
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auction_id")
    private Long id;

    // Many-to-one relationship with User. A user can be a seller of many auctions.
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User seller;

    // Many-to-one relationship with Category. An auction belongs to one category.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "Category of the auction cannot be empty.")
    private Category category;

    // Basic fields for the auction such as title and description.
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;

    // Fields for the starting and current price of the auction.
    @Column(name = "starting_price", columnDefinition = "DECIMAL(10, 2) DEFAULT 0.0", nullable = false)
    private BigDecimal startingPrice;
    @Column(name = "current_price", nullable = false, columnDefinition = "DECIMAL(10, 2) default 0.0")
    private BigDecimal currentPrice;

    // Fields for the start and end time of the auction.
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    // One-to-many relationship with Bid. An auction can have many bids.
    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Bid> bids;

    @Override
    public String toString() {
        return "Auction{" +
                "id=" + id +
                ", seller=" + (seller != null ? seller.getUsername() : "null") +
                ", category=" + (category != null ? category.getCategoryName() : "null") +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startingPrice=" + startingPrice +
                ", currentPrice=" + currentPrice +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auction auction = (Auction) o;
        return Objects.equals(id, auction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}