package com.example.auctionservicesaplication.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

@Controller
public class HomeController {

    @GetMapping("/default")
    public String defaultAfterLogin(Authentication authentication) {
        Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (authorities.contains("ROLE_ADMIN")) {
            return "redirect:/admin/home";
        } else if (authorities.contains("ROLE_USER")) {
            return "redirect:/userhome";
        } else {
            throw new IllegalStateException();
        }
    }

    @GetMapping("/admin/home")
    public String adminHome() {
        return "home"; // your admin home page
    }

    @GetMapping("/userhome")
    public String userHome() {
        return "userhome"; // your user home page
    }

    @GetMapping("/login")
    public String getLogin(){
        return "login";
    }
}
