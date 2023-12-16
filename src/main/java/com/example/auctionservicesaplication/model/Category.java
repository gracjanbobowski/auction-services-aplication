package com.example.auctionservicesaplication.model;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//Category: Reprezentuje kategorię aukcji.

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryId")
    private BigDecimal id;

    @OneToMany(mappedBy = "category")
    private List<Auction> auctions;

    @Column(name = "categoryName")
    private String categoryName;

    @Column(name = "description")
    private String description;



}
