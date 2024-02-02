package com.example.auctionservicesaplication.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

// Custom authentication success handler to redirect users after successful login.
public class SuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectURL = "/";

        // Determine redirect URL based on user roles.
        if (authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))
        ) {
            // Redirect to the root URL if the user has the ADMIN role.
            redirectURL = "/";
        } else if (authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))
        ) {
            // Redirect to the user home page if the user has the USER role.
            redirectURL = "/userhome";
        }
        // Perform the actual redirection to the determined URL.
        response.sendRedirect(redirectURL);
    }
}
