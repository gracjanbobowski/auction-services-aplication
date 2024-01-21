package com.example.auctionservicesaplication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true) // jak byście chcieli nad metodani pisać jakie są dostępy to trzeba to włączyć
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry
                        -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers("/").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/new").hasAnyAuthority("ADMIN")
                        .requestMatchers("/edit/**").hasAnyAuthority("ADMIN")
                        .requestMatchers("/delete/**").hasAuthority("ADMIN")
                        .anyRequest().permitAll());
        return httpSecurity.build();
    }
}
