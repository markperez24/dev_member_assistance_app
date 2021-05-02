package com.member.assistance.core.service;

import com.member.assistance.backend.model.Member;
import com.member.assistance.backend.model.MemberAccount;
import com.member.assistance.core.dto.MemberDetailsDto;
import com.member.assistance.core.dto.MemberDto;
import com.member.assistance.core.dto.MemberListingDto;
import com.member.assistance.core.dto.MemberRegistrationDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public interface MemberService {

    /**
     * List all members
     *
     * @return List
     * @param searchField
     * @param pageIndex
     * @param pageSize
     */
    Map<String, Object> getMembers(String searchField,
                                      Integer pageIndex,
                                      Integer pageSize) throws MemberAssistanceException;

    Map<String, Object> registerMember(MemberRegistrationDto member) throws MemberAssistanceException;

    //TODO:
    String generateOtp(Member member, Integer digit);

    void createKeycloakUser();

    void createMemberLogs(String email, String requestType);

    Member getMember(String username);

    MemberAccount getUsernameFromUserInfo(String emailAddress) throws MemberAssistanceException;

    MemberAccount getUsernameFromUserInfo(Map<String, Object> userInfo) throws MemberAssistanceException;

    void createUpdateProfilePhoto(MemberAccount memberAccount, InputStream is) throws MemberAssistanceException;

    void createUpdateIdPhotos(MemberAccount memberAccount, InputStream frontIs, InputStream backIs) throws MemberAssistanceException;

    ByteArrayResource getProfilePhoto(String accountNumber, String profilePhotoUrl) throws MemberAssistanceException;

    Boolean changePassword(Member member, String password) throws MemberAssistanceException;

    Member updateProfile(Member member, MemberDto updatedMember) throws MemberAssistanceException;

    ByteArrayResource getIdPhoto(String accountNumber, String idPhotoUuid, String side) throws MemberAssistanceException;

    ByteArrayResource getDefaultIdMedia(String side) throws MemberAssistanceException;

    Boolean checkIfExists(String email) throws MemberAssistanceException;

    MemberDetailsDto getMemberDetails(String accountNumber) throws MemberAssistanceException;

    ByteArrayResource getVoucherImage(String accountNumber) throws MemberAssistanceException;

    MemberAccount getMemberAccount(String accountNumber) throws MemberAssistanceException;
}
