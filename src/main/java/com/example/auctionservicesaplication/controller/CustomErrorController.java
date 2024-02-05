package com.example.auctionservicesaplication.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Exception exception = (Exception) request.getAttribute("jakarta.servlet.error.exception");

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("errorCode", statusCode);
            model.addAttribute("errorMessage", exception == null ? "N/A" : exception.getMessage());
        }

        // Determine the redirect URL based on the user's role
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String redirectUrl = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ? "/" : "/userhome";
        model.addAttribute("redirectUrl", redirectUrl);

        return "customError";
    }
}
