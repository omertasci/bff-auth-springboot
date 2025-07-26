package com.example.bffauth.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SharePointService {
    public boolean hasAccess(String token) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            "https://graph.microsoft.com/v1.0/sites/root",
            HttpMethod.GET,
            request,
            String.class
        );

        return response.getStatusCode() == HttpStatus.OK;
    }
}
