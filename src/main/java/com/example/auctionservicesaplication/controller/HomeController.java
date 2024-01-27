package com.example.auctionservicesaplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String userHome() {
        return "home";
    }

    @GetMapping("/login")
    public String getLogin(){
        return "login";
    }
}
