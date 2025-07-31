package com.example.bffauth.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

@Service
public class AzureADService {

    public String getUsernameFromToken(OAuth2AuthenticationToken authentication) {
        return authentication.getPrincipal().getAttribute("name");
    }

    public String getEmailFromToken(OAuth2AuthenticationToken authentication) {
        return authentication.getPrincipal().getAttribute("email");
    }
}
