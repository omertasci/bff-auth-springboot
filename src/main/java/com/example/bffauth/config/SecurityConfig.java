package com.example.bffauth.config;

import com.example.bffauth.azure.AzureAdProperties;
import com.example.bffauth.azure.provider.AzureAdAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/authenticate").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/azure")
                );

        return http.build();
    }

    @Bean
    public DefaultSpringSecurityContextSource contextSource() {
        return new DefaultSpringSecurityContextSource(
                Collections.singletonList("ldap://localhost:389"), "dc=mycompany,dc=com");
    }

    @Bean
    public AuthenticationProvider ldapAuthenticationProvider(DefaultSpringSecurityContextSource contextSource) {
        BindAuthenticator bindAuthenticator = new BindAuthenticator(contextSource);
        bindAuthenticator.setUserDnPatterns(new String[]{"uid={0},ou=users"});

        DefaultLdapAuthoritiesPopulator authoritiesPopulator = new DefaultLdapAuthoritiesPopulator(contextSource, "ou=groups");
        authoritiesPopulator.setIgnorePartialResultException(true);
        authoritiesPopulator.setSearchSubtree(true);

        return new LdapAuthenticationProvider(bindAuthenticator, authoritiesPopulator);
    }

    @Bean
    public AuthenticationProvider azureAdAuthenticationProvider(AzureAdProperties azureAdProperties) {
        return new AzureAdAuthenticationProvider(azureAdProperties);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider ldapAuthenticationProvider, AuthenticationProvider azureAdAuthenticationProvider) {
        // Sıralama önemlidir: Önce LDAP, sonra Azure AD denenir.
        return new ProviderManager(Arrays.asList(ldapAuthenticationProvider, azureAdAuthenticationProvider));
    }
}
