package com.example.auctionservicesaplication.controller;

import com.example.auctionservicesaplication.model.Role;
import com.example.auctionservicesaplication.model.User;
import com.example.auctionservicesaplication.repository.RoleRepository;
import com.example.auctionservicesaplication.repository.UserRepository;
import com.example.auctionservicesaplication.service.EmailService;
import com.example.auctionservicesaplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.Collections;
import java.util.List;


//UserController: Obsługuje żądania związane z użytkownikami.
@Controller
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsManager userDetailsManager;
    private final RoleRepository roleRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository,
                          EmailService emailService, PasswordEncoder passwordEncoder,
                          UserDetailsManager userDetailsManager, RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsManager = userDetailsManager;
        this.roleRepository = roleRepository;
    }

    // Endpoint do wyświetlania listy wszystkich użytkowników
    @GetMapping
    @Secured("ROLE_ADMIN")
    public String getAllUsers(Model model, Principal principal) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);

        // Dodaj informację o zalogowanym użytkowniku
        if (principal != null) {
            model.addAttribute("loggedInUser", principal.getName());
        }

        return "userList";
    }

    // Endpoint to get information about the currently logged-in user
    @GetMapping("/loggedInUser")
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public String getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() instanceof String) {
            // No user is authenticated
            return "Not logged in";
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    // Endpoint do wyświetlania szczegółów danego użytkownika
    @GetMapping("/{userId}")
    @Secured("ROLE_ADMIN")
    public String getUserDetails(@PathVariable Long userId, Model model, Principal principal) {
        User user = userService.getUserById(BigDecimal.valueOf(userId));
        model.addAttribute("user", user);

        // Dodaj informację o zalogowanym użytkowniku
        if (principal != null) {
            model.addAttribute("loggedInUser", principal.getName());
        }

        return "userDetails";
    }

    // Endpoint do wyświetlania formularza rejestracyjnego
    @GetMapping("/register")
    @Secured("ROLE_ADMIN")
    public String getRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registrationForm";
    }

    @PostMapping("/register")
    @Secured("ROLE_ADMIN")
    public String registerUser(@ModelAttribute @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Coś poszło nie tak. Spróbuj ponownie.");
            return "registrationForm";
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "error.user", "Username already exists");
            return "registrationForm";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.registerUser(user);

        UserDetails userDetails = userDetailsManager.loadUserByUsername(user.getUsername());
        userDetailsManager.createUser(userDetails);

        emailService.sendRegistrationConfirmation(user.getEmail(), user.getUsername());

        // Przypisz rolę admina (ROLE_ADMIN) dla określonego warunku
        if (user.isAdmin()) {
            userService.assignAdminRole(user);
        }

        // Dodaj komunikat potwierdzający rejestrację
        model.addAttribute("success", true);
        model.addAttribute("loggedInUser", user.getUsername()); // Dodaj zalogowanego użytkownika

        return "registrationForm";
    }

    // Endpoint do wyświetlania formularza edycji użytkownika
    @GetMapping("/{userId}/edit")
    @Secured("ROLE_ADMIN")
    public String getEditForm(@PathVariable String userId, Model model) {
        Long userIdLong = Long.parseLong(userId);
        User user = userService.getUserById(BigDecimal.valueOf(userIdLong));
        model.addAttribute("user", user);
        return "editForm"; // Załóżmy, że mamy plik HTML o nazwie "editForm.html" z formularzem edycji użytkownika
    }

    // Endpoint obsługujący przesyłanie danych z formularza edycji użytkownika
    @PostMapping("/{userId}/edit")
    @Secured("ROLE_ADMIN")
    public String editUser(@PathVariable String userId, @ModelAttribute User editedUser) {
        Long userIdLong = Long.parseLong(userId);
        userService.editUser(BigDecimal.valueOf(userIdLong), editedUser);
        return "redirect:/users"; // Przekierowanie do listy użytkowników po udanej edycji
    }

    // Endpoint do usuwania użytkownika
    @GetMapping("/{userId}/delete")
    @Secured("ROLE_ADMIN")
    public String deleteUser(@PathVariable String userId) {
        Long userIdLong = Long.parseLong(userId);
        userService.deleteUser(BigDecimal.valueOf(userIdLong));
        return "redirect:/users"; // Przekierowanie do listy użytkowników po udanym usunięciu
    }

    @PostMapping("/{userId}/assignRole")
    @Secured("ROLE_ADMIN")
    public String assignRoleToUser(@PathVariable String userId, @RequestParam String roleName) {
        Long userIdLong = Long.parseLong(userId);
        User user = userService.getUserById(BigDecimal.valueOf(userIdLong));

        // Sprawdź, czy rola o podanej nazwie istnieje
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            // Jeśli nie istnieje, utwórz nową rolę i zapisz ją do bazy danych
            role = new Role(roleName);
            roleRepository.save(role);
        }

        // Sprawdź, czy użytkownik nie ma już przypisanej tej roli
        if (!user.getRoles().contains(role)) {
            // Przypisz rolę do użytkownika
            user.getRoles().add(role);

            // Zapisz użytkownika do bazy danych
            userRepository.save(user);
        }

        return "redirect:/users"; // Przekierowanie do listy użytkowników po udanym przypisaniu roli
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