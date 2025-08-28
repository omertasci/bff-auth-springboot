package com.example.bffauth.config;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.NTCredentials;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

    @Configuration
    public class NtlmRestTemplateConfig {

        @Value("${sharepoint.ag.host}")
        String host;
        @Value("${sharepoint.ag.port}")
        int port;
        @Value("${sharepoint.ag.username}")
        String username;
        @Value("${sharepoint.ag.password}")
        String password;
        @Value("${sharepoint.ag.domain}")
        String domain;

        @Bean
        public RestTemplate restTemplate() {
            BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                    new AuthScope(host, port),
                    new NTCredentials(username, password.toCharArray(), "", domain)
            );

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setDefaultCredentialsProvider(credsProvider)
                    .build();

            HttpComponentsClientHttpRequestFactory requestFactory =
                    new HttpComponentsClientHttpRequestFactory(httpClient);

            return new RestTemplate(requestFactory);
        }
    }