//package com.example.auctionservicesaplication.security;
//
//import com.example.auctionservicesaplication.service.UserDetailsServiceImpl;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfiguration {
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new UserDetailsServiceImpl();
//    }
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService());
//        authProvider.setPasswordEncoder(passwordEncoder());
//
//        return authProvider;
//    }
//
//    @Override
//    protected void
////    @Override
////    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////        auth.authenticationProvider(authenticationProvider());
////    }
////
////    @Override
////    protected void configure(HttpSecurity http) throws Exception {
////        http.authorizeRequests()
////                .requestMatchers("/").hasAnyAuthority("USER", "ADMIN")
////                .requestMatchers("/new").hasAnyAuthority("ADMIN")
////                .requestMatchers("/edit/**").hasAnyAuthority("ADMIN")
////                .requestMatchers("/delete/**").hasAuthority("ADMIN")
////                .anyRequest().authenticated()
////                .and()
////                .formLogin().permitAll()
////                .and()
////                .logout().permitAll()
////                .and()
////                .exceptionHandling().accessDeniedPage("/403")
////        ;
////        ;
////    }
//}
