package com.example.auctionservicesaplication.message;

public class AuctionNotFoundException extends RuntimeException {

    public AuctionNotFoundException(String message) {
        super(message);
    }
}
