package com.example.bffauth.service;

import com.example.bffauth.model.AuthResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    public LDAPService ldapService;

    @Autowired
    public AzureADService azureADService;

    @Autowired
    public SharePointService sharePointService;

    public AuthResult authenticate(String username, String password) {
        if (ldapService.authenticate(username, password)) {
            return AuthResult.success("ldap");
        }

        try {
            String token = azureADService.getAccessToken(username, password);
            if (sharePointService.hasAccess(token)) {
                return AuthResult.success("azure");
            }
        } catch (Exception e) {
            // log
        }

        return AuthResult.failure();
    }
}
