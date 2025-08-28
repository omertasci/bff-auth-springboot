package com.example.bffauth.controller;

import com.example.bffauth.service.AgService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
public class SPController {

    private final AgService agService;

    public SPController(AgService agService) {
        this.agService = agService;
    }


    @GetMapping("/api/data")
    public String data(Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        String domain = (String) authentication.getDetails(); // JwtAuthenticationFilter'ta details'a domain koyduk

        if("ag".equals(domain)){
            return agService.getFiles();
        }

        // burada domain'e göre 3rd-party çağrısı yapılır (RestTemplate/WebClient)
        return "Erişen: " + username + " domain: " + domain;
    }
}