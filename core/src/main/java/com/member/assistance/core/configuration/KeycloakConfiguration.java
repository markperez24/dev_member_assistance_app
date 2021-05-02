package com.member.assistance.core.configuration;

import com.member.assistance.backend.model.ConfigParam;
import com.member.assistance.core.constants.MemberAssistanceConstants;
import com.member.assistance.core.service.ConfigParamService;
import com.member.assistance.core.service.ConfigParamServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

@Configuration
public class KeycloakConfiguration {
    private static final Logger LOGGER = LogManager.getLogger(KeycloakConfiguration.class);

    private static ConfigParamService configParamService = null;
    private static Keycloak keycloak = null;
    private static String serverUrl = null;
    private static String realm = null;
    private static String clientId = null;
    private static String clientSecret = null;
    private static String username = null;
    private static String password = null;

    private final static BiFunction<List<ConfigParam>, String, String> fGetConfigValue
            = (configList, code) -> configList.stream().filter(c->c.getCode().equalsIgnoreCase(code))
            .findFirst().map(ConfigParam::getValue)
            .orElseGet(null);

    @Bean
    ConfigParamService configParamService() {
        if(configParamService == null) {
            configParamService = new ConfigParamServiceImpl();
        }
        return configParamService;
    }

    public static Keycloak getInstance() {
        if (Objects.isNull(serverUrl)) {
            initCredentials();
        }
        if (keycloak == null) {
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .username(username)
                    .password(password)
                    //.resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).register(new CustomJacksonProvider()).build())
                    .resteasyClient(
                            new ResteasyClientBuilder()
                                    .connectionPoolSize(10).build()
                    )
                    .build();
        }
        return keycloak;
    }

    private static void initCredentials() {
        try {
            List<ConfigParam> keycloakAdminConfigList =
                    configParamService.getConfigByType(MemberAssistanceConstants.CONFIG_TYPE_KEYCLOAK_ADMIN_SETTINGS);
            if(CollectionUtils.isNotEmpty(keycloakAdminConfigList)) {
                serverUrl = fGetConfigValue.apply(keycloakAdminConfigList, MemberAssistanceConstants.CONFIG_CODE_KC_ADMIN_SERVER_URL);
                realm = fGetConfigValue.apply(keycloakAdminConfigList, MemberAssistanceConstants.CONFIG_CODE_KC_ADMIN_REALM);
                clientId = fGetConfigValue.apply(keycloakAdminConfigList, MemberAssistanceConstants.CONFIG_CODE_KC_ADMIN_CLIENT_ID);
                clientSecret = fGetConfigValue.apply(keycloakAdminConfigList, MemberAssistanceConstants.CONFIG_CODE_KC_ADMIN_CLIENT_SECRET);
                username = fGetConfigValue.apply(keycloakAdminConfigList, MemberAssistanceConstants.CONFIG_CODE_KC_ADMIN_USERNAME);
                password = fGetConfigValue.apply(keycloakAdminConfigList, MemberAssistanceConstants.CONFIG_CODE_KC_ADMIN_PASSWORD);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static Keycloak getKeycloak() {
        return keycloak;
    }

    public static String getServerUrl() {
        return serverUrl;
    }

    public static String getRealm() {
        return realm;
    }

    public static String getClientId() {
        return clientId;
    }

    public static String getClientSecret() {
        return clientSecret;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }
}
