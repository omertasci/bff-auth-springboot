package com.example.bffauth.azure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "azure.ad")
public class AzureAdProperties {

    private String clientId;
    private String tenantId;
    private String authorityBaseUrl;
    private List<String> scopes;

    // Getters and Setters

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getAuthorityBaseUrl() {
        return authorityBaseUrl;
    }

    public void setAuthorityBaseUrl(String authorityBaseUrl) {
        this.authorityBaseUrl = authorityBaseUrl;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }
}

