package com.example.auctionservicesaplication.controller;

import com.example.auctionservicesaplication.util.ControllerUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model, Authentication authentication) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Exception exception = (Exception) request.getAttribute("jakarta.servlet.error.exception");

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("errorCode", statusCode);
            model.addAttribute("errorMessage", exception == null ? "N/A" : exception.getMessage());
        }

        // Determine the home redirect URL based on the user's role
        model.addAttribute("homeRedirectUrl", ControllerUtil.determineHomeRedirectUrl(authentication));

        return "customError";
    }
}
