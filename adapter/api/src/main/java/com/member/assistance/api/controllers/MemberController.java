package com.member.assistance.api.controllers;

import com.member.assistance.api.exception.CustomResponseEntityExceptionHandler;
import com.member.assistance.api.response.ResponseMemberApi;
import com.member.assistance.api.response.ResponseMessageApi;
import com.member.assistance.backend.model.Member;
import com.member.assistance.backend.model.MemberAccount;
import com.member.assistance.common.utility.FileUtils;
import com.member.assistance.core.constants.MemberAssistanceConstants;
import com.member.assistance.core.dto.ChangePasswordDto;
import com.member.assistance.core.dto.MemberDetailsDto;
import com.member.assistance.core.dto.MemberDto;
import com.member.assistance.core.exception.MemberAssistanceException;
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
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//TODO: move member list to different controller for
/**
 * Member account only controller
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/api/member")
public class MemberController {
    private static Logger LOGGER = LogManager.getLogger(MemberController.class);

    @Autowired
    private UserAuthController userAuthController;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CustomResponseEntityExceptionHandler customResponseEntityExceptionHandler;

    @RequestMapping(method = RequestMethod.GET, value = "/my-profile")
    @ResponseBody
    public Map<String, Object> getMyProfile(@RequestParam("username") final String username) {
        LOGGER.info("Get my profile.");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            MemberAccount memberAccount = memberService
                    .getUsernameFromUserInfo(userAuthController.getUserInfo());
            if(Objects.nonNull(memberAccount)) {
                MemberDto member = new MemberDto().toDto(memberAccount.getMember());
                member.setAccountNumber(memberAccount.getAccountNumber());
                resultMap.put("profile", member);
                resultMap.put("success", "OK");
            } else {
                resultMap.put("error", "Unable to load your profile.");
            }
        } catch (Exception e) {
            resultMap.put("error", "Failed to get member list.");
        }
        return resultMap;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update-my-profile")
    @ResponseBody
    public ResponseEntity<ResponseMemberApi> updateMyProfile(@RequestBody MemberDto updatedMember) {
        LOGGER.info("Update my profile.");
        String message = "";
        try {
            MemberAccount memberAccount = memberService
                    .getUsernameFromUserInfo(userAuthController.getUserInfo());
            if (Objects.nonNull(memberAccount)) {
                Member member = memberService.updateProfile(memberAccount.getMember(), updatedMember);
                if (Objects.nonNull(member)) {
                    LOGGER.info("Profile updated.");
                    message = "Profile has been updated.";
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseMemberApi(MemberAssistanceConstants.STATUS_SUCCESS, message, new MemberDto().toDto(member)));
                } else {
                    throw new MemberAssistanceException("Unable to update profile");
                }
            } else {
                message = MemberAssistanceConstants.ERR_API_MESSAGE_FORBIDDEN;
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMemberApi(MemberAssistanceConstants.STATUS_ERROR, message, null));
            }
        } catch (MemberAssistanceException mae) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMemberApi(MemberAssistanceConstants.STATUS_ERROR, mae.getMessage(), null));
        } catch (Exception e) {
            message = "Unable to updated profile.";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMemberApi(MemberAssistanceConstants.STATUS_ERROR, message, null));
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/upload-profile-photo")
    @ResponseBody
    public ResponseEntity<ResponseMessageApi> uploadProfilePhoto(@RequestParam("file") MultipartFile file) {
        LOGGER.info("Upload profile photo.");
        String message = "";
        try {
            MemberAccount memberAccount = memberService
                    .getUsernameFromUserInfo(userAuthController.getUserInfo());
            if(Objects.nonNull(memberAccount)) {
                memberService.createUpdateProfilePhoto(memberAccount, file.getInputStream());
                message = "Your photo has been successfully added/uploaded.";
            } else {
                message = MemberAssistanceConstants.ERR_API_MESSAGE_FORBIDDEN;
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMessageApi(MemberAssistanceConstants.STATUS_ERROR, message));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessageApi(MemberAssistanceConstants.STATUS_SUCCESS, message));
        } catch (Exception e) {
            message = "Fail to upload profile photo";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessageApi(MemberAssistanceConstants.STATUS_ERROR, message));
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-profile-photo", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getProfilePhoto() {
        LOGGER.info("Get profile photo.");
        try {
            MemberAccount memberAccount = memberService
                    .getUsernameFromUserInfo(userAuthController.getUserInfo());
            if(Objects.nonNull(memberAccount)) {
                LOGGER.info("User account: {}", memberAccount.getAccountNumber());
                String profilePhotoUuid = memberAccount.getProfilePhotoUuid();
                //byte[] media = memberService.getProfilePhoto(profilePhotoUrl);
                ByteArrayResource media = null;
                if(Objects.nonNull(profilePhotoUuid)) {
                    media = memberService.getProfilePhoto(memberAccount.getAccountNumber(), profilePhotoUuid);
                    if(Objects.isNull(media)) {
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

    @RequestMapping(method = RequestMethod.GET, value = "/get-id-photo/{side}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<ByteArrayResource> getIDPhoto(@PathVariable("side") final String side) {
        LOGGER.info("Get id photo: {}", side);
        try {
            MemberAccount memberAccount = memberService
                    .getUsernameFromUserInfo(userAuthController.getUserInfo());
            if(Objects.nonNull(memberAccount)) {
                LOGGER.info("User account: {}", memberAccount.getAccountNumber());
                String idPhotoUuid = memberAccount.getIdPhotoUuid();
                ByteArrayResource media;
                if(Objects.nonNull(idPhotoUuid)) {
                    media = memberService.getIdPhoto(memberAccount.getAccountNumber(), idPhotoUuid, side);
                    if(Objects.isNull(media)) {
                        //redirect to profile default photo;
                        media = memberService.getDefaultIdMedia(side);
                    }
                } else {
                    media = memberService.getDefaultIdMedia(side);
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

    @RequestMapping(method = RequestMethod.POST, value = "/change-password")
    @ResponseBody
    public ResponseEntity<ResponseMessageApi> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        LOGGER.info("Change password.");
        String message = "";
        try {
            MemberAccount memberAccount = memberService
                    .getUsernameFromUserInfo(userAuthController.getUserInfo());
            if (Objects.nonNull(memberAccount)) {
                Boolean changePassword = memberService.changePassword(memberAccount.getMember(), changePasswordDto.getPassword());
                if (Objects.isNull(changePassword) || !changePassword) {
                    message = "Unable to change password.";
                    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessageApi(MemberAssistanceConstants.STATUS_ERROR, message));
                }
                message = "Password successfully changed.";
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessageApi(MemberAssistanceConstants.STATUS_SUCCESS, message));
            } else {
                message = MemberAssistanceConstants.ERR_API_MESSAGE_FORBIDDEN;
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMessageApi(MemberAssistanceConstants.STATUS_ERROR, message));
            }
        } catch (MemberAssistanceException mae) {
            message = mae.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessageApi(MemberAssistanceConstants.STATUS_ERROR, message));
        } catch (Exception e) {
            message = "Unable to change password.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessageApi(MemberAssistanceConstants.STATUS_ERROR, message));
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/upload-id-photo")
    @ResponseBody
    public ResponseEntity<ResponseMessageApi> uploadIDPhoto(@RequestParam("front") MultipartFile front,
                                                                 @RequestParam("back") MultipartFile back) {
        LOGGER.info("Upload id photo.");
        String message = "";
        try {
            MemberAccount memberAccount = memberService
                    .getUsernameFromUserInfo(userAuthController.getUserInfo());
            if(Objects.nonNull(memberAccount)) {
                memberService.createUpdateIdPhotos(memberAccount, front.getInputStream(), back.getInputStream());
                message = "Your photo has been successfully added/uploaded.";
            } else {
                message = MemberAssistanceConstants.ERR_API_MESSAGE_FORBIDDEN;
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMessageApi(MemberAssistanceConstants.STATUS_ERROR, message));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessageApi(MemberAssistanceConstants.STATUS_SUCCESS, message));
        } catch (Exception e) {
            message = "Fail to upload id photo";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessageApi(MemberAssistanceConstants.STATUS_ERROR, message));
        }
    }
}
