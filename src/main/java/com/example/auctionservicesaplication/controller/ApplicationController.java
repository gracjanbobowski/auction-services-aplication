package com.example.auctionservicesaplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicationController {
    @GetMapping("/admin")
    public String home() {
        return "home";
    }
}
