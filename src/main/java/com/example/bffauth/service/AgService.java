package com.example.bffauth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AgService {

  private final RestTemplate restTemplate;

  @Value("${sharepoint.ag.base-url}")
  String baseUrl;

  @Value("${sharepoint.ag.file}")
  String fileUrl;

  public AgService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public String getFiles() {
    String url = baseUrl + fileUrl;
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", "application/json");

    HttpEntity<String> entity = new HttpEntity<>(headers);

    ResponseEntity<String> response =
        restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class);

    return response.getBody();
  }
}
