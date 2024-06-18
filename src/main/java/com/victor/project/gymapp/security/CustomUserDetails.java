package com.victor.project.gymapp.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.AllArgsConstructor;

import java.util.Collection;


/*
 * UserDetails para SpringSecurity personalizada para introducir Uuid's
 */
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final boolean isEnabled;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String uuid;//Campos añadido



    /*
     * Devuelve el Uuid
     */
    public String getUuid() {
        return uuid;
    }

    /*
     * Métodos getter sobreescritos
     * 
     */



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
