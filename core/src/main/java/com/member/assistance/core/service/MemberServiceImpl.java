package com.member.assistance.core.service;

import com.member.assistance.backend.model.ConfigParam;
import com.member.assistance.backend.model.MedicalAssistance;
import com.member.assistance.backend.model.Member;
import com.member.assistance.backend.model.MemberAccount;
import com.member.assistance.backend.model.User;
import com.member.assistance.backend.model.Voucher;
import com.member.assistance.backend.repository.MedicalAssistanceRepository;
import com.member.assistance.backend.repository.MemberAccountRepository;
import com.member.assistance.backend.repository.MemberRepository;
import com.member.assistance.common.utility.FileUtils;
import com.member.assistance.common.utility.IDGeneratorUtils;
import com.member.assistance.common.utility.MemberAssistanceUtils;
import com.member.assistance.core.constants.MedicalAssistanceStatus;
import com.member.assistance.core.constants.MemberAccountStatus;
import com.member.assistance.core.constants.MemberAssistanceConstants;
import com.member.assistance.core.dto.MedicalAssistanceDetailsDto;
import com.member.assistance.core.dto.MemberDetailsDto;
import com.member.assistance.core.dto.MemberDto;
import com.member.assistance.core.dto.MemberListingDto;
import com.member.assistance.core.dto.MemberRegistrationDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class MemberServiceImpl implements MemberService {
    private static Logger LOGGER = LogManager.getLogger(MemberServiceImpl.class);

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberAccountRepository memberAccountRepository;

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private ConfigParamService configParamService;

    @Autowired
    private FileService fileService;

    @Autowired
    private MedicalAssistanceRepository medicalAssistanceRepository;

    //@Autowired
    //private CaptchaServiceImpl captchaService;

    public Map<String, Object> getMembers(String searchField,
                                             Integer pageIndex,
                                             Integer pageSize) throws MemberAssistanceException {
        LOGGER.info("Get members by full name: {} and/or account#: {}.", searchField);
        try {
            Map<String, Object> membersMap = new HashMap<>();
            final Sort.Order sortByLastAppliedDate = new Sort.Order(
                    Sort.Direction.DESC,
                    "lastAppliedDate"
            );
            List<Sort.Order> orders = new ArrayList<>();
            orders.add(sortByLastAppliedDate);
            if (Objects.nonNull(searchField)) {
                /*final Sort.Order sortByFullName = new Sort.Order(
                        Sort.Direction.ASC,
                        "lastName");
                orders.add(sortByFullName);*/

                final Sort.Order sortByAccountNumber = new Sort.Order(
                        Sort.Direction.ASC,
                        "accountNumber"
                );
                orders.add(sortByAccountNumber);
            }
            final Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(
                    orders
            ));
            Page<MemberAccount> memberAccountList = memberAccountRepository.getByFullNameAndAccountNumber(
                    searchField,
                    pageable
            );
            List<MemberListingDto> memberListingDtoList = new ArrayList<>();
            for (MemberAccount memberAccount : memberAccountList.getContent()) {
                MemberListingDto memberListingDto = new MemberListingDto();
                memberListingDto.setAccountNumber(memberAccount.getAccountNumber());
                Member member = memberAccount.getMember();
                final String fullName = member.getFirstName()
                        .concat(" ").concat(member.getMiddleName())
                        .concat(" ").concat(member.getLastName());
                memberListingDto.setFullName(fullName);
                Set<MedicalAssistance> medicalAssistanceSet = memberAccount.getMedicalAssistanceSet();
                memberListingDto.setTotalApplication(0);
                if (CollectionUtils.isNotEmpty(medicalAssistanceSet)) {
                    memberListingDto.setTotalApplication(medicalAssistanceSet.size());
                }
                memberListingDto.setStatus(memberAccount.getApplicationStatus());
                memberListingDtoList.add(memberListingDto);
            }
            membersMap.put("memberList", memberListingDtoList);
            membersMap.put("total", memberAccountList.getTotalElements());

            LOGGER.debug("Searched List: {}", MemberAssistanceUtils
                    .convertObjectToJsonString(memberListingDtoList));

            return membersMap;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Failed to get member lists.");
        }
    }

    @Transactional
    @Override
    public Map<String, Object> registerMember(MemberRegistrationDto memberRegDto) throws MemberAssistanceException {
        LOGGER.info("Register member.");

        Map<String, Object> result = new HashMap<>();
        try {
            final String emailAddress = memberRegDto.getEmailAddress();
            if (Objects.isNull(emailAddress)) {
                throw new MemberAssistanceException("Email address is required.");
            }

            /*Boolean isValidCaptcha = captchaService.isResponseValid(memberRegDto.getCaptcha());
            if(!isValidCaptcha) {
                throw new MemberAssistanceException("Invalid reCaptcha response during registration.");
            }*/

            Member existingMember = memberRepository.findByEmailAddress(emailAddress);
            if (Objects.nonNull(existingMember)) {
                throw new MemberAssistanceException("User already exists.");
            }

            //create new member
            Member member = memberRegDto.toModel();
            member.setActive(Boolean.FALSE);
            member.setVerified(Boolean.FALSE);
            member.setOtp(
                    generateOtp(member, 6)
            );
            member = memberRepository.save(member);
            if (Objects.nonNull(member.getId())) {
                //TODO: generate id number
                //TODO: email otp / send otp via sms

                //generate otp
                //TODO: save user to keycloak once sms or email is verified.
                final String keycloakUserId = userAuthService.addMemberUser(new User(member), memberRegDto);
                if (Objects.nonNull(keycloakUserId)) {
                    //re-save member with keycloak user id
                    member.setKeycloakUserId(keycloakUserId);
                    createMemberAccount(member);
                }
                result.put("isRegistered", Boolean.TRUE);
                result.put("isVerified", Boolean.FALSE);
                return result;
            }
        } catch (MemberAssistanceException mae) {
            throw mae;
        } catch (Exception e) {
            LOGGER.error("System error caused by: {}", e.getMessage(), e);
            throw new MemberAssistanceException("Unable to register user.");
        }
        return null;
    }

    private String createMemberAccount(Member member) {
        final String email = member.getEmailAddress();
        LOGGER.info("Create member account w/ email: {}", email);

        try {
            String accountNumber = generateAccountNumber(email);
            MemberAccount newMemberAcct = memberAccountRepository.findByMember(
                    member
            );
            if (Objects.nonNull(newMemberAcct)) {
                LOGGER.error("Member w/ user: {} already exists.", email);
                throw new MemberAssistanceException("Member already exists.");
            }

            newMemberAcct = new MemberAccount();
            newMemberAcct.setEmailAddress(email);
            newMemberAcct.setAccountNumber(accountNumber);
            newMemberAcct.setAccountStatus(getDefaultAccountStatus());
            newMemberAcct.setMember(member);
            //TODO: NOTE: change configuration w/ id 200
            memberAccountRepository.save(newMemberAcct);
            return newMemberAcct.getAccountNumber();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    private String getDefaultAccountStatus() {
        ConfigParam config = configParamService.getConfigByCodeAndType(
                MemberAssistanceConstants.CODE_DEFAULT_MEMBER_ACCOUNT_APPLICATION_STATUS
                , MemberAssistanceConstants.TYPE_MEMBER_ACCOUNT_CONFIGURATION);
        if (Objects.nonNull(config)) {
            return config.getValue();
        }
        return MemberAccountStatus.IN_REVIEW.getValue();
    }

    private String generateAccountNumber(String email) {
        LOGGER.info("Generate account id for id: {}", email);
        StringBuilder accountNumber = new StringBuilder();

        LocalDateTime now = LocalDateTime.now();
        accountNumber.append(now.getYear())
                .append(IDGeneratorUtils.randomStringDigits(4))
                .append(String.format("%02d", now.getDayOfMonth()))
                .append(IDGeneratorUtils.randomStringDigits(2))
                .append(String.format("%02d", now.getHour()))
                .append(IDGeneratorUtils.randomStringDigits(2))
                .append(String.format("%02d", now.getMinute()))
                .append(IDGeneratorUtils.randomStringDigits(2));
        LOGGER.info("Generated id: {}", accountNumber.toString());

        MemberAccount memberAccount = memberAccountRepository.findByAccountNumber(accountNumber.toString());
        if (Objects.nonNull(memberAccount)) {
            LOGGER.warn("AccountId already exists... Regenerating.");
            generateAccountNumber(email);
        }
        return accountNumber.toString();
    }

    @Override
    public String generateOtp(Member member, Integer digit) {
        try {
            final String emailAddress = member.getEmailAddress();
            LOGGER.info("Generate otp for member: {}", emailAddress);
            //TODO: encrypt
            return IDGeneratorUtils.randomStringDigits(digit);
            //TODO: email/sms
        } catch (Exception e) {
            LOGGER.error("System error caused by: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void createKeycloakUser() {
    }

    //TODO: member logging
    @Override
    public void createMemberLogs(String email, String requestType) {
        LOGGER.debug("Create member logs for user: {}", email);
        try {

        } catch (Exception e) {
            LOGGER.error("System error caused by: {}", e.getMessage(), e);
        }
    }

    @Override
    public Member getMember(String username) {
        Member byEmailAddress = memberRepository.findByEmailAddress(username);
        return byEmailAddress;
    }

    @Override
    public MemberAccount getUsernameFromUserInfo(String emailAddress) throws MemberAssistanceException {
        LOGGER.info("Retrieve member account: {}", emailAddress);
        try {
            Member member = memberRepository.findByEmailAddress(emailAddress);
            if (Objects.isNull(member)) {
                throw new MemberAssistanceException("Unable to retrieve member.");
            }

            MemberAccount memberAccount = member.getMemberAccount();
            if (Objects.isNull(memberAccount)) {
                memberAccount = memberAccountRepository.findByMember(
                        member
                );
            }
            if (Objects.isNull(member)) {
                throw new MemberAssistanceException("Unable to retrieve member account.");
            }
            return memberAccount;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public MemberAccount getUsernameFromUserInfo(Map<String, Object> userInfo) throws MemberAssistanceException {
        LOGGER.info("Get username from user info.");
        try {
            final String emailAddress = (String) userInfo.get("username");
            return getUsernameFromUserInfo(emailAddress);
        } catch (MemberAssistanceException mae) {
            throw mae;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Transactional
    @Override
    public Member updateProfile(Member member, MemberDto updatedMember) throws MemberAssistanceException {
        LOGGER.info("Update profile for member: {}", member.getEmailAddress());
        try {
            member.setAge(updatedMember.getAge());
            member.setAddress1(updatedMember.getAddress1());
            member.setAddress2(updatedMember.getAddress2());
            member.setCity(updatedMember.getCity());
            member.setProvince(updatedMember.getProvince());
            member.setContactNumber(updatedMember.getContactNumber());
            return memberRepository.save(member);
        } catch (Exception e) {
            throw new MemberAssistanceException("Unable to update profile.");
        }
    }

    @Transactional
    @Override
    //TODO: add encryption of url
    public void createUpdateProfilePhoto(MemberAccount memberAccount, InputStream is) throws MemberAssistanceException {
        final String accountNumber = memberAccount.getAccountNumber();
        LOGGER.info("Save profile photo of member: {}", memberAccount.getAccountNumber());
        try {
            String currProfilePhotoUuid = memberAccount.getProfilePhotoUuid();

            Boolean isNew = Boolean.FALSE;
            if (Objects.isNull(currProfilePhotoUuid)) {
                isNew = Boolean.TRUE;
                currProfilePhotoUuid = UUID.randomUUID().toString();
            }

            Resource resource = fileService.createUpdateProfilePhoto(accountNumber, currProfilePhotoUuid, is, isNew);
            if (isNew && Objects.nonNull(resource.exists())) {
                LOGGER.info("Created and save new profile with uuid: {}", currProfilePhotoUuid);
                memberAccount.setProfilePhotoUuid(currProfilePhotoUuid);
                memberAccount.setAccountStatus(verifyAccountStatus(memberAccount));
                memberAccountRepository.save(memberAccount);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Unable to save profile photo.");
        }
    }

    @Override
    public void createUpdateIdPhotos(MemberAccount memberAccount, InputStream frontIs, InputStream backIs) throws MemberAssistanceException {
        final String accountNumber = memberAccount.getAccountNumber();
        LOGGER.info("Create/update id photos w/ account# {}", accountNumber);
        try {
            String currIdPhotoUuid = memberAccount.getIdPhotoUuid();

            Boolean isNew = Boolean.FALSE;
            if (Objects.isNull(currIdPhotoUuid)) {
                isNew = Boolean.TRUE;
                currIdPhotoUuid = UUID.randomUUID().toString();
            }
            List<Resource> resources = fileService.createUpdateIdPhotos(accountNumber, currIdPhotoUuid,
                    frontIs, backIs, isNew);
            if (isNew && resources.size() == 2) {
                LOGGER.info("Created and save new id photo with uuid: {}", currIdPhotoUuid);
                memberAccount.setIdPhotoUuid(currIdPhotoUuid);
                memberAccount.setAccountStatus(verifyAccountStatus(memberAccount));
                memberAccountRepository.save(memberAccount);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Unable to save profile photo.");
        }
    }

    @Override
    //TODO: decrypt of url when encryption if needed
    public ByteArrayResource getProfilePhoto(String accountNumber,
                                             String profilePhotoUuid) throws MemberAssistanceException {
        LOGGER.info("Get profile photo with uuid: {}", profilePhotoUuid);
        try {
            return fileService.getProfilePhoto(accountNumber, profilePhotoUuid);
        } catch (MemberAssistanceException mae) {
            throw mae;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Unable to get profile photo");
        }
    }

    @Override
    public ByteArrayResource getVoucherImage(String accountNumber) throws MemberAssistanceException {
        LOGGER.info("Get latest voucher image with account number: {}", accountNumber);

        ByteArrayResource byteArrayResource = null;
        try {
            MemberAccount memberAccount = memberAccountRepository.findByAccountNumber(accountNumber);

            Set<MedicalAssistance> medicalAssistanceSet = memberAccount.getMedicalAssistanceSet();
            if (CollectionUtils.isNotEmpty(medicalAssistanceSet)) {
                MedicalAssistance latestMedicalAssistance = medicalAssistanceSet.stream().max(Comparator.comparing(MedicalAssistance::getId)).orElse(null);
                String voucherNumber = latestMedicalAssistance.getVoucherNumber();
                if(latestMedicalAssistance.getStatus().equalsIgnoreCase(MedicalAssistanceStatus.APPROVED.getValue())
                    || Objects.nonNull(voucherNumber)
                ) {
                    Voucher voucher = latestMedicalAssistance.getVoucher();
                    String voucherUuid = voucher.getVoucherUuid();
                    byteArrayResource = fileService.getVoucherImage(voucherNumber, voucherUuid);
                }
            }
            return byteArrayResource;
        } catch (MemberAssistanceException mae) {
            throw mae;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Failed to get image voucher.");
        }
    }

    @Override
    public ByteArrayResource getIdPhoto(String accountNumber, String idPhotoUuid, String side) throws MemberAssistanceException {
        LOGGER.info("Get id photo w/ side: {}", side);
        try {
            return fileService.getIdPhoto(accountNumber, idPhotoUuid, side);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Failed to get id photos.");
        }
    }

    @Transactional
    @Override
    public Boolean changePassword(Member member,
                                  String password) throws MemberAssistanceException {
        LOGGER.info("Change password with member: {}", member.getEmailAddress());
        try {
            //TODO: set logging
            return userAuthService.changePassword(member.getKeycloakUserId(), password);
        } catch (MemberAssistanceException mae) {
            throw mae;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Unable to change password.");
        }
    }

    @Override
    public ByteArrayResource getDefaultIdMedia(String side) throws MemberAssistanceException {
        LOGGER.info("Get default id media.");
        try {
            Map<String, ByteArrayResource> mediaMap = new HashMap<>();
            ByteArrayResource byteArrayResource;
            //front
            if(MemberAssistanceConstants.KEY_ID_FRONT.equalsIgnoreCase(side)) {
                //mediaMap.put(MemberAssistanceConstants.KEY_ID_FRONT,
                //        new ByteArrayResource(FileUtil.downloadFileFromUrl(new URL(MemberAssistanceConstants.ID_FRONT_IMAGE_DEFAULT_URL))));
                byteArrayResource = new ByteArrayResource(FileUtils.downloadFileFromUrl(new URL(MemberAssistanceConstants.ID_FRONT_IMAGE_DEFAULT_URL)));
            //back
            } else {
                //mediaMap.put(MemberAssistanceConstants.KEY_ID_BACK,
                //        new ByteArrayResource(FileUtil.downloadFileFromUrl(new URL(MemberAssistanceConstants.ID_BACK_IMAGE_DEFAULT_URL))));
                byteArrayResource = new ByteArrayResource(FileUtils.downloadFileFromUrl(new URL(MemberAssistanceConstants.ID_BACK_IMAGE_DEFAULT_URL)));
            }
            return byteArrayResource;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Failed to load default id media.");
        }
    }

    @Override
    public Boolean checkIfExists(String email) throws MemberAssistanceException {
        LOGGER.info("Check if user exists.");
        Boolean exists = Boolean.FALSE;
        try {
            Member member = memberRepository.findByEmailAddress(email);
            if (Objects.nonNull(member)) {
                exists = Boolean.TRUE;
            }
            return exists;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Failed to check if user exists.");
        }
    }

    @Override
    public MemberAccount getMemberAccount(String accountNumber) throws MemberAssistanceException {
        LOGGER.info("Get member account w/ account number: {}", accountNumber);
        try {
            return memberAccountRepository.findByAccountNumber(accountNumber);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Failed to check if user exists.");
        }
    }

    private String verifyAccountStatus(MemberAccount memberAccount) {
        String accountStatus = memberAccount.getAccountStatus();
        try {
            if(Objects.nonNull(memberAccount.getIdPhotoUuid())
                    && Objects.nonNull(memberAccount.getProfilePhotoUuid())) {
                accountStatus = MemberAccountStatus.VERIFIED.getValue();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return accountStatus;
    }

    public MemberDetailsDto getMemberDetails(String accountNumber) throws MemberAssistanceException {
        LOGGER.info("Get member details by full name: {} and/or account#: {}.", accountNumber);
        try {
            MemberAccount memberAccount = memberAccountRepository.findByAccountNumber(accountNumber);

            //Meember Details
            MemberDetailsDto memberDetailsDto = new MemberDetailsDto();
            memberDetailsDto.setAccountNumber(memberAccount.getAccountNumber());
            final String fullName = memberAccount.getMember().getFirstName()
                        .concat(" ").concat(memberAccount.getMember().getMiddleName())
                        .concat(" ").concat(memberAccount.getMember().getLastName());
            memberDetailsDto.setFullName(fullName);
            final String fullAddress = memberAccount.getMember().getAddress1()
                        .concat(" ").concat(memberAccount.getMember().getAddress2())
                        .concat(" ").concat(memberAccount.getMember().getCity())
                        .concat(" ").concat(memberAccount.getMember().getProvince());
            memberDetailsDto.setFullAddress(fullAddress);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
            String date = simpleDateFormat.format(memberAccount.getMember().getDateOfBirth());
            memberDetailsDto.setDateOfBirth(date);
            memberDetailsDto.setGender(memberAccount.getMember().getGender());
            memberDetailsDto.setContactNumber(memberAccount.getMember().getContactNumber());            

            //Medical Assistance Summary
            List<MedicalAssistanceDetailsDto> medicalDetailsDto = new ArrayList<>();
            Set<MedicalAssistance> summary = memberAccount.getMedicalAssistanceSet();
            Iterator<MedicalAssistance> it = summary.iterator();
            while(it.hasNext()){
                MedicalAssistance assistance = it.next();
                MedicalAssistanceDetailsDto details = new MedicalAssistanceDetailsDto();
                details.setFirstName(assistance.getFirstName());
                details.setMiddleName(Objects.isNull(assistance.getMiddleName())?"":assistance.getMiddleName());
                details.setLastName(assistance.getLastName());
                details.setVoucherNumber(assistance.getVoucherNumber());
                details.setDateAwarded(assistance.getDateAwarded());
                details.setAmount(assistance.getAmount());
                details.setMemberId(assistance.getMemberAccount().getAccountNumber());
                medicalDetailsDto.add(details);
            }
            memberDetailsDto.setMedicalAssistanceList(medicalDetailsDto);

            LOGGER.debug("Selected Member Details: {}", MemberAssistanceUtils
                    .convertObjectToJsonString(memberDetailsDto));

            return memberDetailsDto;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Unable to get member details.");
        }
    }

}
