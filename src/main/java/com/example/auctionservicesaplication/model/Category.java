package com.example.auctionservicesaplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Represents a category of auctions.
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    // One-to-many relationship with Auction. A category can have multiple auctions.
    @OneToMany(mappedBy = "category")
    private List<Auction> auctions;

    // Fields for the name and description of the category.
    @Column(name = "category_name")
    private String categoryName;
    private String description;
}
