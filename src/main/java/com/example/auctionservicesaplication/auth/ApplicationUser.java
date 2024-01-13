package com.example.auctionservicesaplication.auth;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class ApplicationUser implements UserDetails {

    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> grantedAuthorities;
    private final boolean isAccountNotExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCreadentialsNonExpired;
    private final boolean isEnabled;

    public ApplicationUser(String username,
                           String password,
                           Collection<? extends GrantedAuthority> grantedAuthorities,
                           boolean isAccountNotExpired,
                           boolean isAccountNonLocked,
                           boolean isCreadentialsNonExpired,
                           boolean isEnabled) {
        this.username = username;
        this.password = password;
        this.grantedAuthorities = grantedAuthorities;
        this.isAccountNotExpired = isAccountNotExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCreadentialsNonExpired = isCreadentialsNonExpired;
        this.isEnabled = isEnabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNotExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCreadentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

}
