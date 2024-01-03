package com.example.auctionservicesaplication.service;

import com.example.auctionservicesaplication.message.UserNotFoundException;
import com.example.auctionservicesaplication.model.User;
import com.example.auctionservicesaplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


//EmailService: Serwis obsługujący wysyłanie powiadomień email (np. potwierdzenia rejestracji).
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(BigDecimal userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    public void registerUser(User user) {
        // Dodaj logikę walidacji i przetwarzania danych rejestracyjnych
        userRepository.save(user);
    }

    public void editUser(BigDecimal userId, User editedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Dodaj logikę edycji danych użytkownika
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
}

//    W powyższym kodzie:
//        getAllUsers: Metoda pobiera wszystkich użytkowników z bazy danych, wykorzystując metodę findAll z interfejsu UserRepository.

//        getUserById: Metoda pobiera użytkownika o określonym ID z bazy danych, używając metody findById z interfejsu UserRepository.
//        Jeśli użytkownik nie istnieje, rzucany jest wyjątek UserNotFoundException.

//        registerUser: Metoda dodaje nowego użytkownika do bazy danych, używając metody save z interfejsu UserRepository.
//        Przed zapisaniem użytkownika można dodać logikę walidacji danych rejestracyjnych.
//
//        editUser: Metoda edytuje istniejącego użytkownika na podstawie przekazanych danych, używając metody save z interfejsu UserRepository.
//        Jeśli użytkownik nie istnieje, rzucany jest wyjątek UserNotFoundException.
//
//        deleteUser: Metoda usuwa istniejącego użytkownika z bazy danych, używając metody delete z interfejsu UserRepository.
//        Jeśli użytkownik nie istnieje, rzucany jest wyjątek UserNotFoundException.