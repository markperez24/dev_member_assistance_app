package com.member.assistance.core.service;

import com.member.assistance.backend.model.User;
import com.member.assistance.core.configuration.KeycloakConfiguration;
import com.member.assistance.core.constants.MemberAssistanceConstants;
import com.member.assistance.core.dto.MemberRegistrationDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

@Service
public class UserAuthServiceImpl implements UserAuthService {
    Logger LOGGER = LogManager.getLogger(UserAuthServiceImpl.class);

    @Autowired
    ConfigParamService configParamService;

    @Override
    public String addMemberUser(User user, MemberRegistrationDto member) throws MemberAssistanceException {
        LOGGER.info("Add member user.");
        try {
            UsersResource usersResource = KeycloakConfiguration
                    .getInstance()
                    .realm(KeycloakConfiguration.getRealm())
                    .users();
            //TODO: generate password to send via email after otp success
            CredentialRepresentation credentialRepresentation = createPasswordCredentials(member.getPassword());

            UserRepresentation kcUser = new UserRepresentation();
            kcUser.setUsername(user.getEmail());
            kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
            kcUser.setFirstName(user.getFirstName());
            kcUser.setLastName(user.getLastName());
            kcUser.setEmail(user.getEmail());
            kcUser.setEmailVerified(Boolean.TRUE);
            kcUser.setAttributes(Collections.singletonMap("origin",
                    Arrays.asList("registration")));
            kcUser.setAttributes(Collections.singletonMap("mobile_number",
                    Arrays.asList(member.getContactNumber())));
            kcUser.setEnabled(Boolean.TRUE);

            Response response = usersResource.create(kcUser);
            LOGGER.info("Add member user response status: {}, response info: {}",
                    response.getStatus(), response.getStatusInfo().getReasonPhrase());

            final String userId = CreatedResponseUtil.getCreatedId(response);
            if (Objects.nonNull(userId)) {
                addMemberUserRole(usersResource, userId);
                //configure user required actions such as otp
                addRequiredUserActions(usersResource, userId);
            }
            return userId;
        } catch (MemberAssistanceException mae) {
            throw mae;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Unable to create user.");
        }
    }

    @Override
    public RoleRepresentation getRoleOnRealm(String role) {
        LOGGER.info("Get role on realm: {}", role);
        try {
            RealmResource realmResource = KeycloakConfiguration.getInstance()
                    .realm(KeycloakConfiguration.getRealm());

            RoleRepresentation roleRepresentation = realmResource.roles()
                    .get(role).toRepresentation();

            return roleRepresentation;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void addMemberUserRole(UsersResource usersResource,
                                  String userId) {
        LOGGER.info("Add member user role w/ userId: {}", userId);
        try {
            RoleRepresentation memberRole = getRoleOnRealm("Member");
            if (Objects.nonNull(memberRole)) {
                UserResource userResource = usersResource.get(userId);
                userResource.roles().realmLevel() //
                        .add(Arrays.asList(memberRole));
                LOGGER.info("Successfully added member role.");
            } else {
                LOGGER.warn("Unable to add member role to user: {}", userId);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public Boolean changePassword(String keycloakUserId,
                                  String password) throws MemberAssistanceException {
        LOGGER.info("Change password w/ keycloak user id: {}.", keycloakUserId);
        try {
            CredentialRepresentation passwordCredentials = createPasswordCredentials(password);
            UsersResource usersResource = KeycloakConfiguration
                    .getInstance()
                    .realm(KeycloakConfiguration.getRealm())
                    .users();
            UserResource userResource = usersResource.get(keycloakUserId);
            // Set new password credential
            userResource.resetPassword(passwordCredentials);
            return Boolean.TRUE;
        } catch (Exception e) {
            throw new MemberAssistanceException("Unable to change password.");
        }
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        LOGGER.info("Create password credentials.");
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(Boolean.FALSE);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    private void addRequiredUserActions(UsersResource usersResource, String userId) throws MemberAssistanceException {
        LOGGER.info("Add required user actions");
        try {
            String configValue = configParamService.getConfigValue(MemberAssistanceConstants.CONFIG_CODE_CONFIGURE_OTP,
                    MemberAssistanceConstants.CONFIG_TYPE_KEYCLOAK_USER_SETTINGS);
            Boolean enabledOtp = Boolean.parseBoolean(Objects.toString(configValue, "false"));
            if(enabledOtp.equals(Boolean.TRUE)) {
                usersResource.get(userId).executeActionsEmail(Arrays.asList("CONFIGURE_OTP"));
            }
        } catch (Exception e) {
            throw new MemberAssistanceException("Unable to add user required actions.");
        }
    }
}
