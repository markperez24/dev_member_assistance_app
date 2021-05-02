package com.member.assistance.api.controllers;


import com.member.assistance.backend.model.ConfigParam;
import com.member.assistance.backend.model.Member;
import com.member.assistance.core.dto.CaptchaRequestDto;
import com.member.assistance.core.dto.MemberRegistrationDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import com.member.assistance.core.captcha.CaptchaServiceImpl;
import com.member.assistance.core.exception.ReCaptchaException;
import com.member.assistance.core.service.ConfigParamService;
import com.member.assistance.core.service.MemberService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping(value = "/public")
public class PublicController {
    private static Logger LOGGER = LogManager.getLogger(PublicController.class);

    @Autowired
    private ConfigParamService configParamService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CaptchaServiceImpl captchaService;

    @RequestMapping(method = RequestMethod.GET, value = "/get-video-details")
    @ResponseBody
    public Map<String, Object> getVideoDetails() {
        LOGGER.info("Get video details.");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<ConfigParam> videoSettings = configParamService.getConfigByType("VIDEO_SETTINGS");

            if (!CollectionUtils.isEmpty(videoSettings)) {
                resultMap.put("videoUrl", videoSettings.stream().filter(vs -> vs.getCode().equalsIgnoreCase("VIDEO_URL")).findFirst().map(ConfigParam::getValue));
                resultMap.put("videoMessage", videoSettings.stream().filter(vs -> vs.getCode().equalsIgnoreCase("VIDEO_MESSAGE")).findFirst().map(ConfigParam::getValue));
                resultMap.put("videoUrlId", videoSettings.stream().filter(vs -> vs.getCode().equalsIgnoreCase("VIDEO_URL_ID")).findFirst().map(ConfigParam::getValue));
                resultMap.put("success", "OK");
            } else {
                resultMap.put("error", "Video settings is null.");
            }
        } catch (Exception e) {
            resultMap.put("error", "Failed to get member list.");
        }
        return resultMap;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> register(@RequestBody MemberRegistrationDto member) {
        LOGGER.info("Register user.");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, Object> registeredMap = this.memberService.registerMember(member);
            if (Objects.nonNull(registeredMap)) {
                resultMap.putAll(registeredMap);
                resultMap.put("success", "OK");
                return ResponseEntity.status(HttpStatus.OK).body(resultMap);
            } else {
                resultMap.put("error", "Unable to register user.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
            }
        } catch (MemberAssistanceException mae) {
            resultMap.put("error", mae.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultMap.put("error", "Failed to register user.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/email-exists/{email}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> register(
            @PathVariable("email") String email
    ) {
        LOGGER.info("Check if email exists: {}", email);
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap.put("exists", this.memberService.checkIfExists(email));
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } catch (MemberAssistanceException mae) {
            resultMap.put("error", mae.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultMap.put("error", "Failed to register user.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/verify-captcha")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verifyCaptcha(@RequestBody CaptchaRequestDto captchaDto) {
        LOGGER.info("Verify captcha...");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, Object> captchaMap = this.captchaService.processResponse(captchaDto.getReCaptcha());
            if (Objects.nonNull(captchaMap)) {
                resultMap.putAll(captchaMap);
                return ResponseEntity.status(HttpStatus.OK).body(resultMap);
            } else {
                resultMap.put("error", "Failed processing reCaptcha. Please try again.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
            }
        } catch (ReCaptchaException ex) {
            resultMap.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultMap.put("error", "Failed processing reCaptcha. Please try again.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        }
    }
}
