package com.example.auctionservicesaplication.message;

// Custom exception class for handling cases where a user is not found.
public class UserNotFoundException extends RuntimeException {

    // Constructor for the exception. Takes a message that describes the exception detail.
    public UserNotFoundException(String message) {
        super(message); // Calls the constructor of the superclass (RuntimeException) with the provided message.
    }
}
