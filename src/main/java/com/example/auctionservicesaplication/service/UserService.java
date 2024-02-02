package com.example.auctionservicesaplication.service;

import com.example.auctionservicesaplication.message.UserNotFoundException;
import com.example.auctionservicesaplication.model.Role;
import com.example.auctionservicesaplication.model.User;
import com.example.auctionservicesaplication.repository.RoleRepository;
import com.example.auctionservicesaplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Service class for handling business logic related to users.
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    // Constructor for dependency injection of repositories and password encoder.
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    // Retrieves all users from the repository.
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Retrieves a user by their ID or throws an exception if not found.
    public User getUserById(BigDecimal userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    // Registers a new user, encoding their password and assigning roles.
    public void registerUser(User user) {
        // Validates if the user object is not null.
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        // Checks if the username already exists in the database to prevent duplicate usernames.
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserNotFoundException("Username already exists: " + user.getUsername());
        }

        // Encrypts the user's password using a password encoder for security.
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Enables the user's account for access.
        user.setEnabled(true);

        // Assigns the default role 'ROLE_USER' to the new user.
        // If the role doesn't exist in the database, it's created.
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role("ROLE_USER");
            roleRepository.save(userRole);
        }

        // Adds the role to the user and saves the user in the database.
        user.getRoles().add(userRole);
        userRepository.save(user);
    }

    // Assigns the admin role to an existing user.
    public void assignAdminRole(User user) {
        // Fetches the 'ROLE_ADMIN' from the database or creates it if it doesn't exist.
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        if (adminRole == null) {
            adminRole = new Role("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        // Adds the admin role to the user if they don't already have it.
        if (!user.getRoles().contains(adminRole)) {
            user.getRoles().add(adminRole);
            userRepository.save(user);
        }
    }

    // Retrieves a user by their username or throws an exception if not found.
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
    }

    // Edits an existing user's details.
    @Transactional
    public void editUser(BigDecimal userId, User editedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        existingUser.setUsername(editedUser.getUsername());
        existingUser.setEmail(editedUser.getEmail());
        existingUser.setPassword(editedUser.getPassword());

        userRepository.save(existingUser);
    }

    // Deletes a user by their ID.
    public void deleteUser(BigDecimal userId) {
        User userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        userRepository.delete(userToDelete);
    }

    // Loads a user's details by their username for Spring Security authentication.
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        // Fetches the user from the database by the given username.
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Converts the roles of the user into Spring Security's GrantedAuthority objects.
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        // Constructs a Spring Security User object (from org.springframework.security.core.userdetails.User)
        // with details extracted from the user fetched from the database. This includes the username,
        // password, enabled status, and authorities (roles).
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true, // Account is non-expired
                true, // Credentials are non-expired
                true, // Account is non-locked
                authorities
        );
    }

}
