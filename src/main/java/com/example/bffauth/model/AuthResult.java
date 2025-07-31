package com.example.bffauth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@AllArgsConstructor
public class AuthResult {
    private String username;
    private Collection<? extends GrantedAuthority> authorities;

}
