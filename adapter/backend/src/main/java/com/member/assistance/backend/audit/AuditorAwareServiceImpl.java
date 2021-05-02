package com.member.assistance.backend.audit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuditorAwareServiceImpl implements AuditorAware<String>, AuditorAwareService {
    public static final Logger LOGGER = LogManager.getLogger(AuditorAwareServiceImpl.class);

    private String currentUser;

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(Objects.isNull(authentication)) {
            LOGGER.info("User not authenticated.");
            return Optional.ofNullable(currentUser);
        }

        Object principal = authentication.getPrincipal();
        if(authentication.getPrincipal() instanceof String) {
            String authUser = (String) principal;
            //should only be for member registration
            if(authUser.equalsIgnoreCase("anonymousUser")) {
                setCurrentUser("publicUser");
            }
        }

        if(authentication instanceof KeycloakAuthenticationToken) {
            KeycloakAuthenticationToken keycloakAuth = (KeycloakAuthenticationToken) authentication;
            Principal keycloakAuthPrincipal = (Principal) keycloakAuth.getPrincipal();
            if(Objects.nonNull(keycloakAuthPrincipal)) {
                setCurrentUser(keycloakAuthPrincipal.getName());
            } else {
                setCurrentUser("system");
            }
        }
        return Optional.of(this.currentUser);
    }

    @Override
    public void setCurrentUser(String user) {
        this.currentUser = user;
    }
}
