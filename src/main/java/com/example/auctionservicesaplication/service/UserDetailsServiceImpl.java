package com.example.auctionservicesaplication.service;

import com.example.auctionservicesaplication.model.User;
import com.example.auctionservicesaplication.repository.UserRepository;
import com.example.auctionservicesaplication.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username)
       throws UsernameNotFoundException {
       User user = userRepository.getUserByUsername(username);

       if (user == null) {
           throw new UsernameNotFoundException("Could not find user");
       }
       return new MyUserDetails(user);
   }

   @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
   }


}
