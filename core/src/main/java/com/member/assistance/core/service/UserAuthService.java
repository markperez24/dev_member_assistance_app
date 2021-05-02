package com.member.assistance.core.service;

import com.member.assistance.backend.model.User;
import com.member.assistance.core.dto.MemberRegistrationDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;


/**
 * User Auth Service Interface for keycloak users.
 * References:
 * https://gist.github.com/thomasdarimont/c4e739c5a319cf78a4cff3b87173a84b
 * https://medium.com/chain-analytica/keycloak-create-users-for-a-realm-in-spring-boot-3eff924a8db1
 */
public interface UserAuthService {
    /**
     * Add member user.
     * @param user
     * @param member
     * @return
     */
    String addMemberUser(User user, MemberRegistrationDto member) throws MemberAssistanceException;

    /**
     * Add member role.
     * @param usersResource
     * @param userId
     */
    void addMemberUserRole(UsersResource usersResource,
                           String userId);

    /**
     * Get realm role.
     * @param role
     * @return
     */
    RoleRepresentation getRoleOnRealm(String role);

    Boolean changePassword(String member, String password) throws MemberAssistanceException;
}
