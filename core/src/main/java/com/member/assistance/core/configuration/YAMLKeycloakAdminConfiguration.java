package com.member.assistance.core.configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class YAMLKeycloakAdminConfiguration {
    private String kcServerUrl;
    private String kcRealm;
    private String kcClientId;
    private String kcClientSecret;
    private String kcUsername;
    private String kcPassword;

    public String getKcServerUrl() {
        return kcServerUrl;
    }

    public void setKcServerUrl(String kcServerUrl) {
        this.kcServerUrl = kcServerUrl;
    }

    public String getKcRealm() {
        return kcRealm;
    }

    public void setKcRealm(String kcRealm) {
        this.kcRealm = kcRealm;
    }

    public String getKcClientId() {
        return kcClientId;
    }

    public void setKcClientId(String kcClientId) {
        this.kcClientId = kcClientId;
    }

    public String getKcClientSecret() {
        return kcClientSecret;
    }

    public void setKcClientSecret(String kcClientSecret) {
        this.kcClientSecret = kcClientSecret;
    }

    public String getKcUsername() {
        return kcUsername;
    }

    public void setKcUsername(String kcUsername) {
        this.kcUsername = kcUsername;
    }

    public String getKcPassword() {
        return kcPassword;
    }

    public void setKcPassword(String kcPassword) {
        this.kcPassword = kcPassword;
    }
}
