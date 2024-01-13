package com.example.auctionservicesaplication.controller;

import com.example.auctionservicesaplication.model.User;
import com.example.auctionservicesaplication.repository.UserRepository;
import com.example.auctionservicesaplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


//UserController: Obsługuje żądania związane z użytkownikami.
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // Endpoint do wyświetlania listy wszystkich użytkowników
    @GetMapping
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "userList"; // Załóżmy, że mamy plik HTML o nazwie "userList.html" do wyświetlenia listy użytkowników
    }

    // Endpoint do wyświetlania szczegółów danego użytkownika
    @GetMapping("/{userId}")
    public String getUserDetails(@PathVariable String userId, Model model) {
        Long userIdLong = Long.parseLong(userId);
        User user = userService.getUserById(BigDecimal.valueOf(userIdLong));
        model.addAttribute("user", user);
        return "userDetails"; // Załóżmy, że mamy plik HTML o nazwie "userDetails.html" do wyświetlenia szczegółów użytkownika
    }

    // Endpoint do wyświetlania formularza rejestracyjnego
    @GetMapping("/register")
    public String getRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registrationForm"; // Załóżmy, że mamy plik HTML o nazwie "registrationForm.html" z formularzem rejestracyjnym
    }

    // Endpoint obsługujący przesyłanie danych z formularza rejestracyjnego
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.registerUser(user);
        return "redirect:/users"; // Przekierowanie do listy użytkowników po udanej rejestracji
    }

    // Endpoint do wyświetlania formularza edycji użytkownika
    @GetMapping("/{userId}/edit")
    public String getEditForm(@PathVariable String userId, Model model) {
        Long userIdLong = Long.parseLong(userId);
        User user = userService.getUserById(BigDecimal.valueOf(userIdLong));
        model.addAttribute("user", user);
        return "editForm"; // Załóżmy, że mamy plik HTML o nazwie "editForm.html" z formularzem edycji użytkownika
    }

    // Endpoint obsługujący przesyłanie danych z formularza edycji użytkownika
    @PostMapping("/{userId}/edit")
    public String editUser(@PathVariable String userId, @ModelAttribute User editedUser) {
        Long userIdLong = Long.parseLong(userId);
        userService.editUser(BigDecimal.valueOf(userIdLong), editedUser);
        return "redirect:/users"; // Przekierowanie do listy użytkowników po udanej edycji
    }

    // Endpoint do usuwania użytkownika
    @GetMapping("/{userId}/delete")
    public String deleteUser(@PathVariable String userId) {
        Long userIdLong = Long.parseLong(userId);
        userService.deleteUser(BigDecimal.valueOf(userIdLong));
        return "redirect:/users"; // Przekierowanie do listy użytkowników po udanym usunięciu
    }
}

// fixMe : Byłem tu :)

//Opisy:
//
//        getAllUsers: Endpoint do wyświetlania listy wszystkich użytkowników.
//        getUserDetails: Endpoint do wyświetlania szczegółów danego użytkownika.
//        getRegistrationForm: Endpoint do wyświetlania formularza rejestracyjnego.
//        registerUser: Endpoint obsługujący przesyłanie danych z formularza rejestracyjnego.
//        getEditForm: Endpoint do wyświetlania formularza edycji użytkownika.
//        editUser: Endpoint obsługujący przesyłanie danych z formularza edycji użytkownika.
//        deleteUser: Endpoint do usuwania użytkownika.
//        Uwaga: Nazwy plików HTML (np. "userList.html", "userDetails.html", "registrationForm.html", "editForm.html")
//        są przykładowe i powinny zostać dostosowane do struktury projektu i używanego silnika szablonów (np. Thymeleaf).