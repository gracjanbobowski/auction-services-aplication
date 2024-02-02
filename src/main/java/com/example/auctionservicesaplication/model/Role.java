package com.example.auctionservicesaplication.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Represents a role assigned to a user in the system.
@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    // The name of the role (e.g., ROLE_USER, ROLE_ADMIN).
    @Column(name = "name")
    private String name;

    // Default constructor and constructor with role name.
    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }
}
