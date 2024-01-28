package com.example.auctionservicesaplication.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    // Obsługuje żądanie dostępu do strony głównej administratora.
    @GetMapping("/")
    @Secured("ROLE_ADMIN")
    public String adminHome() {
        return "home";
    }

    // Obsługuje żądanie dostępu do strony logowania.
    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/userhome")
    @Secured("ROLE_USER")
    public String userHome(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Tutaj możesz dodać kod do pobrania dodatkowych informacji o zalogowanym użytkowniku
        // np. userService.getUserByUsername(username)

        model.addAttribute("loggedInUser", username);
        return "userhome";
    }
}

