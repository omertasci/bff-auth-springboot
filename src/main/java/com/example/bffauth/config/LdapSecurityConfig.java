package com.example.bffauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;

import java.util.Arrays;

//@Configuration
public class LdapSecurityConfig {

/*    @Bean
    public DefaultSpringSecurityContextSource contextSource() {
        return new DefaultSpringSecurityContextSource(
                Arrays.asList("ldap://localhost:389"),
                "dc=mycompany,dc=com"
        );
    }

    @Bean
    public AuthenticationManager authenticationManager(DefaultSpringSecurityContextSource contextSource) throws Exception {
        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(null);

        builder
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .contextSource(contextSource);

        return builder.build();
    }*/
}
