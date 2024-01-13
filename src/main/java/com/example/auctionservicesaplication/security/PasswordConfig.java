package com.example.auctionservicesaplication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.sql.DataSource;

import static org.springframework.transaction.TransactionDefinition.withDefaults;

@Configuration
@EnableWebSecurity
public class PasswordConfig {
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder(16);
//    }

//    @Bean
//    public UserDetailsManager userDetailsManager(DataSource dataSource) {
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//        jdbcUserDetailsManager.setUsersByUsernameQuery(
//                "select username, password, true from users where username = ?");
//        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
//                "select username, authority from authorities where username = ?");
//        return jdbcUserDetailsManager;
//    }

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/home")
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().hasRole("ADMIN")
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET,  "/home").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/home").hasAnyRole("OWNER")
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl("/home.html"); // Ustaw domyślny URL po udanym uwierzytelnieniu
        return handler;
    }
}
