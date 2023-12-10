package com.example.auctionservicesaplication.model;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

//User: Reprezentuje użytkownika.
public class User {



    private BigDecimal id;
    private String name;
    private String lastName;
    private String emailAddress;
    private String password;

    private Set<Bid> bids = new HashSet<>();



}
