package com.member.assistance.api.controllers;

import com.member.assistance.core.exception.MemberAssistanceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class UserAuthController {
    private static Logger LOGGER = LogManager.getLogger(UserAuthController.class);

    @RequestMapping(name = "/admin/user-info")
    Map<String, Object> getUserInfo() throws MemberAssistanceException {
        Map<String, Object> userInfo = new HashMap<>();
        try {
            KeycloakAuthenticationToken authentication = (KeycloakAuthenticationToken)
                    SecurityContextHolder.getContext().getAuthentication();

            Principal principal = (Principal) authentication.getPrincipal();
            if(Objects.isNull(principal)) {
                throw new MemberAssistanceException("Unable to retrieve user info.");
            }
            userInfo.put("username", principal.getName());
        } catch (Exception e) {
            LOGGER.error("System error caused by: {}", e.getMessage());
            LOGGER.error(e);
        }
        return userInfo;
    }
}
