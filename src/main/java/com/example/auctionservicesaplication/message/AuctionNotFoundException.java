package com.example.auctionservicesaplication.message;

// Custom exception class for handling cases where an auction is not found.
public class AuctionNotFoundException extends RuntimeException {

    // Constructor for the exception. Takes a message that describes the exception detail.
    public AuctionNotFoundException(String message) {
        super(message); // Calls the constructor of the superclass (RuntimeException) with the provided message.
    }
}