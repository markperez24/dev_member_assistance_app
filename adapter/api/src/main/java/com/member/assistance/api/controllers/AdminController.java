package com.member.assistance.api.controllers;

import com.member.assistance.backend.model.MemberAccount;
import com.member.assistance.backend.repository.MemberAccountRepository;
import com.member.assistance.common.utility.FileUtils;
import com.member.assistance.core.constants.MemberAssistanceConstants;
import com.member.assistance.core.dto.MemberApplicationDto;
import com.member.assistance.core.dto.MemberDetailsDto;
import com.member.assistance.core.dto.MemberListingDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import com.member.assistance.core.service.MedicalAssistanceService;
import com.member.assistance.core.service.MemberService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//TODO: move member list to different controller for

/**
 * Member account only controller
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api/admin")
public class AdminController {
    private static Logger LOGGER = LogManager.getLogger(MemberController.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private MedicalAssistanceService medicalAssistanceService;

    @RequestMapping(method = RequestMethod.GET, value = "/get-member-list")
    @ResponseBody
    public ResponseEntity<?> getMembers(
            @RequestParam(value = "searchField", required = false) final String searchField,
            @RequestParam(value = "pageIndex", required = false) final Integer pageIndex,
            @RequestParam(value = "pageSize", required = false) final Integer pageSize
    ) {
        LOGGER.info("Get members.");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap.putAll(memberService.getMembers(searchField, pageIndex, pageSize));
            resultMap.put("success", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } catch (MemberAssistanceException mae) {
            LOGGER.error(mae.getMessage(), mae);
            resultMap.put("error", "Failed to retrieve member list.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultMap.put("error", "Failed to list members.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/deny-application/")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> denyApplication(
            @RequestBody MemberListingDto memberListingDto
    ) {
        LOGGER.info("Deny application.");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap.put("member", medicalAssistanceService.denyApplication(memberListingDto));
            resultMap.put("success", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } catch (MemberAssistanceException mae) {
            LOGGER.error(mae.getMessage(), mae);
            resultMap.put("error", "Failed to deny application.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultMap.put("error", "Failed to deny application.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-medical-application")
    @ResponseBody
    public ResponseEntity<?> getApplication(
            @RequestParam("accountNumber") String accountNumber
    ) {
        LOGGER.info("Get application.");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap.put("application", medicalAssistanceService.getApplication(accountNumber));
            resultMap.put("success", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } catch (MemberAssistanceException mae) {
            LOGGER.error(mae.getMessage(), mae);
            resultMap.put("error", "Failed to get application.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultMap.put("error", "Failed to get application.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/approve-application/")
    @ResponseBody
    public ResponseEntity<?> approveApplication(
            @RequestBody MemberApplicationDto memberApplicationDto
    ) {
        LOGGER.info("Approve application.");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap.put("member", medicalAssistanceService.approveApplication(memberApplicationDto));
            resultMap.put("success", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } catch (MemberAssistanceException mae) {
            LOGGER.error(mae.getMessage(), mae);
            resultMap.put("error", mae.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultMap.put("error", "Failed to approve application.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-member-details")
    @ResponseBody
    public ResponseEntity<?> getMemberDetails(
            @RequestParam(value = "member", required = false) final String accountNumber
    ) {
        LOGGER.info("Get member details.");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            MemberDetailsDto memberDetails = memberService.getMemberDetails(accountNumber);
            resultMap.put("memberDetails", memberDetails);
            resultMap.put("success", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } catch (MemberAssistanceException mae) {
            LOGGER.error(mae.getMessage(), mae);
            resultMap.put("error", "Failed to retrieve member details.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultMap.put("error", "Failed to fetch details of the selected member.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-profile-photo-by-account-number/{accountNumber}"
            , produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getProfilePhotoByAccountNumber(
            @PathVariable(value = "accountNumber") final String accountNumber) {
        LOGGER.info("Get profile photo.");
        try {
            MemberAccount memberAccount = memberService
                    .getMemberAccount(accountNumber);
            if (Objects.nonNull(memberAccount)) {
                LOGGER.info("User account: {}", memberAccount.getAccountNumber());
                String profilePhotoUuid = memberAccount.getProfilePhotoUuid();
                //byte[] media = memberService.getProfilePhoto(profilePhotoUrl);
                ByteArrayResource media = null;
                if (Objects.nonNull(profilePhotoUuid)) {
                    media = memberService.getProfilePhoto(memberAccount.getAccountNumber(), profilePhotoUuid);
                    if (Objects.isNull(media)) {
                        //redirect to profile default photo;
                        media = new ByteArrayResource(FileUtils.downloadFileFromUrl(new URL(MemberAssistanceConstants.PROFILE_IMAGE_DEFAULT_URL)));
                    }
                } else {
                    media = new ByteArrayResource(FileUtils.downloadFileFromUrl(new URL(MemberAssistanceConstants.PROFILE_IMAGE_DEFAULT_URL)));
                }
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG);
                //headers.setCacheControl(CacheControl.noCache().getHeaderValue());
                return new ResponseEntity<>(media, headers, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-voucher-image/{accountNumber}"
            , produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getVoucherImage(
            @PathVariable(value = "accountNumber") final String accountNumber
    ) {
        LOGGER.info("Get voucher image.");
        try {
            ByteArrayResource media = memberService.getVoucherImage(accountNumber);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            //headers.setCacheControl(CacheControl.noCache().getHeaderValue());
            return new ResponseEntity<>(media, headers, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

