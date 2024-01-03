package com.example.auctionservicesaplication.message;

public class BidNotFoundException extends RuntimeException {

    public BidNotFoundException(String message) {
        super(message);
    }
}
