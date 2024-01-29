package com.example.auctionservicesaplication.security;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class JdbcSecurityConfig {

//    @Bean
//    public UserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
//        return new JdbcUserDetailsManager(dataSource);

//    }
        @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//    @Bean
//    public InitializingBean initializingBean(DataSource dataSource){
//        return () -> {
//            String password = new BCryptPasswordEncoder().encode("1234");
////            UserDetails admin = User.withUsername("admin")
////                    .password(password)
////                    .roles("USER","ADMIN")
////                    .build();
////            jdbcUserDetailsManager(dataSource).createUser(admin);
//
//            UserDetails user = User.withUsername("user")
//                    .password(password)
//                    .roles("USER")
//                    .build();
//            jdbcUserDetailsManager(dataSource).createUser(user);
//        };
    }

