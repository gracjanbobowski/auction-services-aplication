package com.example.auctionservicesaplication.controller;

import com.example.auctionservicesaplication.model.Authority;
import com.example.auctionservicesaplication.model.Role;
import com.example.auctionservicesaplication.model.User;
import com.example.auctionservicesaplication.repository.AuthorityRepository;
import com.example.auctionservicesaplication.repository.RoleRepository;
import com.example.auctionservicesaplication.repository.UserRepository;
import com.example.auctionservicesaplication.service.EmailService;
import com.example.auctionservicesaplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.List;
import java.util.Optional;

// UserController handles all user-related requests.
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
    private final AuthorityRepository authorityRepository;

    // Constructor for dependency injection.
    @Autowired
    public UserController(UserService userService, UserRepository userRepository,
                          EmailService emailService, PasswordEncoder passwordEncoder,
                          UserDetailsManager userDetailsManager, RoleRepository roleRepository, AuthorityRepository authorityRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsManager = userDetailsManager;
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
    }

    // Handles request for fetching a list of all users.
    @GetMapping
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "userList";
    }

    // Handles request for fetching detailed information about a user.
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

    // Handles request for accessing the admin home page.
    @GetMapping("/admin/home")
    @Secured("ROLE_ADMIN")
    public String adminHome(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User loggedInUser = userService.getUserByUsername(username);

            if (loggedInUser != null) {
                model.addAttribute("loggedInUser", loggedInUser);
            }
        }
        return "home";
    }

    // Handles request for accessing the user home page.
    @GetMapping("/userhome")
    @Secured("ROLE_USER")
    public String userHome(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User loggedInUser = userService.getUserByUsername(username);

        if (loggedInUser != null) {
            model.addAttribute("loggedInUser", loggedInUser);
        }
        return "userhome";
    }

    // Handles request for fetching detailed information about a user.
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

    // Handles request for accessing the new user registration form.
    @GetMapping("/register")
    @Secured("ROLE_ADMIN")
    public String getRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registrationForm";
    }

    // Handles the new user registration process. This method is secured, allowing only admins to execute it.
    @PostMapping("/register")
    @Secured("ROLE_ADMIN")
    public String registerUser(@ModelAttribute @Valid User user, BindingResult bindingResult, Model model) {
        // Checks for form validation errors. If errors are found, returns to the registration form with an error message.
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Something went wrong. Please try again.");
            return "registrationForm";
        }

        // Checks if a user with the same username already exists in the database.
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        // If a user with the same username is found, rejects the submission and returns to the registration form.
        if (existingUser.isPresent()) {
            bindingResult.rejectValue("username", "error.user", "Username already exists");
            return "registrationForm";
        }

        // Encodes the password using the password encoder and enables the user account.
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        // Checks if the 'ROLE_USER' exists in the database. If not, creates and saves the new role.
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role("ROLE_USER");
            roleRepository.save(userRole);
        }

        // Adds the 'ROLE_USER' to the user's set of roles and saves the user in the database.
        user.getRoles().add(userRole);
        userRepository.save(user);

        // Creates an authority record for the user with 'ROLE_USER' and saves it.
        Authority userAuthority = new Authority(user.getUsername(), "ROLE_USER");
        authorityRepository.save(userAuthority);

        // Adds a success attribute to the model and redirects to the users list, indicating the registration was successful.
        model.addAttribute("success", true);
        model.addAttribute("loggedInUser", user.getUsername());

        return "redirect:/users";
    }

    // Handles request for accessing the user edit form.
    @GetMapping("/{userId}/edit")
    @Secured("ROLE_ADMIN")
    public String getEditForm(@PathVariable String userId, Model model) {
        Long userIdLong = Long.parseLong(userId);
        User user = userService.getUserById(BigDecimal.valueOf(userIdLong));
        model.addAttribute("user", user);
        return "editForm";
    }

    // Handles the user edit process.
    @PostMapping("/{userId}/edit")
    @Secured("ROLE_ADMIN")
    public String editUser(@PathVariable String userId, @ModelAttribute User editedUser) {
        Long userIdLong = Long.parseLong(userId);
        userService.editUser(BigDecimal.valueOf(userIdLong), editedUser);
        return "redirect:/users";
    }

    // Handles request for deleting a user.
    @GetMapping("/{userId}/delete")
    @Secured("ROLE_ADMIN")
    public String deleteUser(@PathVariable String userId) {
        Long userIdLong = Long.parseLong(userId);
        userService.deleteUser(BigDecimal.valueOf(userIdLong));
        return "redirect:/users";
    }

    // Handles request for assigning a role to a user.
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
