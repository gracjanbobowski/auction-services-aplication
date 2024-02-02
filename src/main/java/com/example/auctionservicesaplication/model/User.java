package com.example.auctionservicesaplication.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.GenerationType;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Represents a user of the application.
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    // One-to-many relationships indicating the user's participation in auctions, bids, and transaction ratings.
    @OneToMany(mappedBy = "seller")
    private List<Auction> auctions;
    @OneToMany(mappedBy = "bidder")
    private List<Bid> bids;
    @OneToMany(mappedBy = "reviewer")
    private List<TransactionRating> transactionRatings;

    // Fields for user's username, email, and password. Validation annotations ensure that these fields are not empty.
    @NotEmpty(message = "Username is required")
    private String username;
    @NotEmpty(message = "Email is required")
    private String email;
    @NotEmpty(message = "Password is required")
    private String password;

    // Indicates whether the user's account is enabled.
    @Column(nullable = false)
    private boolean enabled;

    // Many-to-many relationship with Role. A user can have multiple roles.
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Utility methods to check if the user has specific roles.
    public boolean isAdmin() {
        return roles.stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }

    public boolean isUser() {
        return roles.stream().anyMatch(role -> role.getName().equals("ROLE_USER"));
    }
}
