package com.example.auctionservicesaplication.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Handles the request to access the admin home page. Only accessible by users with 'ROLE_ADMIN'.
    @GetMapping("/")
    @Secured("ROLE_ADMIN")
    public String adminHome() {
        return "home"; // Returns the home view for admin users
    }

    // Handles the request to access the login page.
    @GetMapping("/login")
    public String getLogin() {
        return "login"; // Returns the login view
    }

    // Handles the request to access the home page for regular users. Only accessible by users with 'ROLE_USER'.
    @GetMapping("/userhome")
    @Secured("ROLE_USER")
    public String userHome(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Adds the username to the model to be used in the view
        model.addAttribute("loggedInUser", username);
        return "userhome"; // Returns the home view for regular users
    }
}
