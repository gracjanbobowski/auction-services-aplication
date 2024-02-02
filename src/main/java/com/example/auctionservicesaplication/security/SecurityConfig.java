package com.example.auctionservicesaplication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

// Main configuration class for Spring Security.
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    // Configures the security filter chain for HTTP requests.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic(Customizer.withDefaults()) // Enables basic HTTP authentication.
                .csrf(AbstractHttpConfigurer::disable) // Disables CSRF protection.
                .authorizeHttpRequests((authorize) -> authorize
                        // Configure access rules for different URL patterns.
                        .requestMatchers("/home").hasAnyAuthority("ROLE_ADMIN") // Only admins can access /home.
                        .requestMatchers("/userhome").hasAnyAuthority("ROLE_USER") // Only users can access /userhome.
                        .requestMatchers("/").permitAll() // Anyone can access the root URL.
                        .anyRequest().authenticated() // All other requests require authentication.
                )
                // Configures form-based login.
                .formLogin(login -> login
                        .loginPage("/login") // Custom login page URL.
                        .successHandler(new SuccessHandler()) // Use custom success handler.
                        .permitAll()) // Allow everyone to access the login page.
                // Configures logout behavior.
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") // Redirect to login page on successful logout.
                        .invalidateHttpSession(true) // Invalidate the session.
                        .deleteCookies("JSESSIONID") // Delete session cookies.
                        .permitAll()); // Allow everyone to access the logout functionality.

        return httpSecurity.build(); // Build the configured security filter chain.
    }

    // Bean for configuring JDBC-based user details service.
    @Bean
    public UserDetailsManager userDetailsService() {
        return new JdbcUserDetailsManager(dataSource);
    }

}
