package com.example.auctionservicesaplication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/userhome").hasAuthority("ROLE_USER")
                        .requestMatchers("/login", "/resources/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/default", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
    @Bean
    public UserDetailsManager userDetailsService() {
        return new JdbcUserDetailsManager(dataSource);
    }
}


//    @Bean
//    public SwitchUserFilter switchUserFilter() {
//        SwitchUserFilter switchUserFilter = new SwitchUserFilter();
//        switchUserFilter.setUserDetailsService(userDetailsService());
//        switchUserFilter.setSwitchUserUrl("/switchUser");
//        switchUserFilter.setExitUserUrl("/exitSwitchUser");
//        switchUserFilter.setTargetUrl("/");
//        return switchUserFilter;
//    }







//    @Bean
//    UserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
//        return new JdbcUserDetailsManager(dataSource);
//    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        // Konfiguracja użytkowników
//        auth.inMemoryAuthentication()
//                .withUser("admin").password("{noop}admin").roles("ADMIN")
//                .and()
//                .withUser("user").password("{noop}user").roles("USER");
//    }


//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }


//    @Bean
//    public UserDetailsManager userDetailsManager() {
//        JdbcUserDetailsManagerConfigurer<HttpSecurity> jdbcUserDetailsConfigurer = new JdbcUserDetailsManagerConfigurer<>();
//        jdbcUserDetailsConfigurer.dataSource(dataSource).passwordEncoder(NoOpPasswordEncoder.getInstance());
//        JdbcUserDetailsManager userDetailsManager = jdbcUserDetailsConfigurer.getUserDetailsService();
//        return userDetailsManager;
//    }



//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("admin").password("a").roles("USER");
//    }
//}



//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .httpBasic(Customizer.withDefaults())
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry
//                        -> authorizationManagerRequestMatcherRegistry
//                        .requestMatchers("/").hasAnyAuthority("USER", "ADMIN")
//                        .requestMatchers("/new").hasAnyAuthority("ADMIN")
//                        .requestMatchers("/edit/**").hasAnyAuthority("ADMIN")
//                        .requestMatchers("/delete/**").hasAuthority("ADMIN")
//                        .anyRequest().permitAll());
//        return httpSecurity.build();
//    }
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.builder()
//                .username("user")
//                .password("{noop}password") // Spring Security 5+ requires password storage format
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{noop}password")
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }
//}