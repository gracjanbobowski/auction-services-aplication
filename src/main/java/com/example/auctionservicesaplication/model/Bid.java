package com.example.auctionservicesaplication.model;

//Bid: Reprezentuje ofertę licytacyjną.


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bid {

    private BigDecimal id;

    private Auction auction;
    private User user;
    private BigDecimal amount;

    private LocalDateTime bidTime;
}
