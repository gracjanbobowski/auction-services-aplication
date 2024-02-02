package com.example.auctionservicesaplication.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Represents the authority granted to a user in the system.
@Getter
@Setter
@Entity
@Table(name = "authorities")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private Long id;

    // The username the authority is associated with.
    @Column(name = "username", nullable = false)
    private String username;

    // The specific authority (e.g., ROLE_USER, ROLE_ADMIN) granted to the user.
    @Column(name = "authority", nullable = false)
    private String authority;

    // Many-to-one relationship with User. A user can have multiple authorities.
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Default constructor and constructor with parameters.
    public Authority() {
    }

    public Authority(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }
}
