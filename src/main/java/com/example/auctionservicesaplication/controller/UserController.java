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
import java.util.Optional;


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

    @GetMapping
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "userList";
    }

    @GetMapping("/{userId}/details")
    @Secured("ROLE_ADMIN")
    public String getUserDetails(@PathVariable BigDecimal userId, Model model, Principal principal) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);

        if (principal != null) {
            model.addAttribute("loggedInUser", principal.getName());
        }

        return "userDetails";
    }

    @GetMapping("/home")
    @Secured("ROLE_USER")
    public String userHome(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("loggedInUser", principal.getName());
        }

        // Reszta kodu obsługującego stronę główną

        return "userhome";
    }

    @GetMapping("/admin/home")
    @Secured("ROLE_ADMIN")
    public String adminHome(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("loggedInUser", principal.getName());
        }

        // Reszta kodu obsługującego stronę główną

        return "home";
    }

    @GetMapping("/loggedInUser")
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public String getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() instanceof String) {
            return "Not logged in";
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    @GetMapping("/{userId}")
    @Secured("ROLE_ADMIN")
    public String getUserDetails(@PathVariable Long userId, Model model, Principal principal) {
        User user = userService.getUserById(BigDecimal.valueOf(userId));
        model.addAttribute("user", user);

        if (principal != null) {
            model.addAttribute("loggedInUser", principal.getName());
        }

        return "userDetails";
    }

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
            model.addAttribute("error", "Something went wrong. Please try again.");
            return "registrationForm";
        }

        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            // Obsługa sytuacji, gdy istnieje już użytkownik o tej nazwie
            bindingResult.rejectValue("username", "error.user", "Username already exists");
            return "registrationForm";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.registerUser(user);

        UserDetails userDetails = userDetailsManager.loadUserByUsername(user.getUsername());
        userDetailsManager.createUser(userDetails);

        emailService.sendRegistrationConfirmation(user.getEmail(), user.getUsername());

        if (user.isAdmin()) {
            userService.assignAdminRole(user);
        }

        model.addAttribute("success", true);
        model.addAttribute("loggedInUser", user.getUsername());

        return "redirect:/users";
    }

    @GetMapping("/{userId}/edit")
    @Secured("ROLE_ADMIN")
    public String getEditForm(@PathVariable String userId, Model model) {
        Long userIdLong = Long.parseLong(userId);
        User user = userService.getUserById(BigDecimal.valueOf(userIdLong));
        model.addAttribute("user", user);
        return "editForm";
    }

    @PostMapping("/{userId}/edit")
    @Secured("ROLE_ADMIN")
    public String editUser(@PathVariable String userId, @ModelAttribute User editedUser) {
        Long userIdLong = Long.parseLong(userId);
        userService.editUser(BigDecimal.valueOf(userIdLong), editedUser);
        return "redirect:/users";
    }

    @GetMapping("/{userId}/delete")
    @Secured("ROLE_ADMIN")
    public String deleteUser(@PathVariable String userId) {
        Long userIdLong = Long.parseLong(userId);
        userService.deleteUser(BigDecimal.valueOf(userIdLong));
        return "redirect:/users";
    }

    @PostMapping("/{userId}/assignRole")
    @Secured("ROLE_ADMIN")
    public String assignRoleToUser(@PathVariable BigDecimal userId, @RequestParam String roleName) {
        User user = userService.getUserById(userId);

        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            role = new Role(roleName);
            roleRepository.save(role);
        }

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        }

        return "redirect:/users";
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