package com.example.auctionservicesaplication.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

// Utility class for common controller functionality
public class ControllerUtil {

    // Utility method to determine the redirect URL based on user's role
    public static String determineHomeRedirectUrl(Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "/";
        } else {
            return "/userhome";
        }
    }
}
