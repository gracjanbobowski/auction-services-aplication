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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


//EmailService: Serwis obsługujący wysyłanie powiadomień email (np. potwierdzenia rejestracji).
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;

    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(BigDecimal userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    public void registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserNotFoundException("Username already exists: " + user.getUsername());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        // Sprawdź, czy rola o nazwie "ROLE_USER" istnieje
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            // Jeśli nie istnieje, utwórz nową rolę i zapisz ją do bazy danych
            userRole = new Role("ROLE_USER");
            roleRepository.save(userRole);
        }

        // Dodaj rolę "ROLE_USER" do użytkownika
        user.getRoles().add(userRole);

        // Zapisz użytkownika do bazy danych
        userRepository.save(user);
    }

    public void assignAdminRole(User user) {
        // Sprawdź, czy rola o nazwie "ROLE_ADMIN" istnieje
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        if (adminRole == null) {
            // Jeśli nie istnieje, utwórz nową rolę i zapisz ją do bazy danych
            adminRole = new Role("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        // Sprawdź, czy użytkownik nie ma już roli "ROLE_ADMIN"
        if (!user.getRoles().contains(adminRole)) {
            // Dodaj rolę "ROLE_ADMIN" do użytkownika
            user.getRoles().add(adminRole);

            // Zapisz użytkownika do bazy danych
            userRepository.save(user);
        }
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
    }

    @Transactional
    public void editUser(BigDecimal userId, User editedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Aktualizuj dane użytkownika
        existingUser.setUsername(editedUser.getUsername());
        existingUser.setEmail(editedUser.getEmail());
        existingUser.setPassword(editedUser.getPassword());

        userRepository.save(existingUser);
    }

    public void deleteUser(BigDecimal userId) {
        User userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Dodaj logikę usuwania użytkownika
        userRepository.delete(userToDelete);
    }

    // Dodaj tę metodę, aby uzyskać UserDetails na podstawie nazwy użytkownika
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                authorities
        );
    }
}