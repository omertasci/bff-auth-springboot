package com.example.bffauth.azure.provider;

import com.example.bffauth.azure.AzureAdProperties;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.PublicClientApplication;
import com.microsoft.aad.msal4j.UserNamePasswordParameters;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class AzureAdAuthenticationProvider implements AuthenticationProvider {

    private final AzureAdProperties properties;

    public AzureAdAuthenticationProvider(AzureAdProperties properties) {
        this.properties = properties;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        try {
            IAuthenticationResult result = acquireToken(username, password);
            return new UsernamePasswordAuthenticationToken(username, password, null);
        } catch (Exception e) {
            throw new BadCredentialsException("Azure AD authentication failed: " + e.getMessage(), e);
        }
    }

    private IAuthenticationResult acquireToken(String username, String password)
            throws MalformedURLException, ExecutionException, InterruptedException {

        String authority = properties.getAuthorityBaseUrl() + "/" + properties.getTenantId();

        PublicClientApplication app = PublicClientApplication.builder(properties.getClientId())
                .authority(authority)
                .build();

        UserNamePasswordParameters parameters = UserNamePasswordParameters.builder(
                        Set.copyOf(properties.getScopes()),
                        username,
                        password.toCharArray())
                .build();

        return app.acquireToken(parameters).get();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

