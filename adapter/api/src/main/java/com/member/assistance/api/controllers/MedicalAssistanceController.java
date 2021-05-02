package com.member.assistance.api.controllers;

import com.member.assistance.backend.model.MedicalAssistance;
import com.member.assistance.backend.model.MemberAccount;
import com.member.assistance.core.constants.MedicalAssistanceStatus;
import com.member.assistance.core.constants.MemberAccountApplicationStatus;
import com.member.assistance.core.constants.MemberAccountStatus;
import com.member.assistance.core.constants.VoucherStatus;
import com.member.assistance.core.dto.MedicalAssistanceApplicationDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import com.member.assistance.core.service.HospitalService;
import com.member.assistance.core.service.MedicalAssistanceService;
import com.member.assistance.core.service.MemberService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.common.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping(value = "api/medical-assistance")
public class MedicalAssistanceController {
    private static Logger LOGGER = LogManager.getLogger(MedicalAssistanceController.class);

    @Autowired
    private MedicalAssistanceService medicalAssistanceService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserAuthController userAuthController;

    @Autowired
    private HospitalService hospitalService;

    @RequestMapping(method = RequestMethod.GET, value = "/apply")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> apply() {
        LOGGER.info("Apply.");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            MemberAccount account = memberService.getUsernameFromUserInfo(userAuthController.getUserInfo());
            if (Objects.isNull(account)) {
                throw new MemberAssistanceException("Cannot retrieve member account.");
            }
            final String applicationStatus = account.getApplicationStatus();
            final String accountStatus = account.getAccountStatus();
            if (
                    MemberAccountApplicationStatus.ALLOWED.getValue().equalsIgnoreCase(applicationStatus) ||
                            (MemberAccountStatus.VERIFIED.getValue().equalsIgnoreCase(accountStatus) &&
                                    Objects.isNull(applicationStatus))
            ) {
                resultMap.put("hospitals", medicalAssistanceService.getAvailableHospitals());
                resultMap.put("configurations", medicalAssistanceService.getLookupConfigurations());
                resultMap.put("success", "OK");
                return ResponseEntity.status(HttpStatus.OK).body(resultMap);
            } else {
                resultMap.put("error", "Your account is currently not allowed to apply for an assistance.");
                resultMap.put("home", "Go back home.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
            }
        } catch (MemberAssistanceException mae) {
            LOGGER.error(mae.getMessage(), mae);
            resultMap.put("error", mae.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        } catch (Exception e) {
            resultMap.put("error", "Something went wrong. Please try again.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/view")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> viewMemberAssistance(
            @RequestParam(value = "pageIndex", required = false) final Integer pageIndex,
            @RequestParam(value = "pageSize", required = false) final Integer pageSize
    ) {
        LOGGER.info("View medical assistance.");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            MemberAccount account = memberService.getUsernameFromUserInfo(userAuthController.getUserInfo());
            if (Objects.isNull(account)) {
                throw new MemberAssistanceException("Cannot retrieve member account.");
            }
            final String accountStatus = account.getAccountStatus();
            final String applicationStatus = account.getApplicationStatus();
            List<MedicalAssistance> medicalAssistanceList = medicalAssistanceService
                    .getMedicalAssistanceByMember(account, pageIndex, pageSize);
            MedicalAssistance latestApplication = medicalAssistanceList.stream().max(Comparator.comparing(MedicalAssistance::getId))
                    .orElse(null);
            String currApplicationStatus = null;
            if (Objects.nonNull(latestApplication)) {
                currApplicationStatus = latestApplication.getStatus();
            }

            resultMap.put("medicalAssistanceList", medicalAssistanceList);
            if(MemberAccountStatus.IN_REVIEW.getValue().equalsIgnoreCase(accountStatus)) {
                resultMap.put("inReview", accountStatus);
                resultMap.put("requirements", medicalAssistanceService.getRequirementsList());
            }
            resultMap.put("isAllowed",
                    (Objects.isNull(currApplicationStatus) ||
                            currApplicationStatus.equalsIgnoreCase(MedicalAssistanceStatus.CLOSED.getValue())) &&
                            MemberAccountStatus.VERIFIED.getValue().equalsIgnoreCase(accountStatus));
            resultMap.put("isDenied",
                    Objects.nonNull(currApplicationStatus) &&
                            currApplicationStatus.equalsIgnoreCase(MedicalAssistanceStatus.DENIED.getValue()) &&
                            MemberAccountApplicationStatus.DENIED.getValue().equalsIgnoreCase(applicationStatus));
            resultMap.put("isProcessing",
                    Objects.nonNull(currApplicationStatus) &&
                            currApplicationStatus.equalsIgnoreCase(MedicalAssistanceStatus.PROCESSING.getValue()) &&
                            MemberAccountApplicationStatus.PROCESSING.getValue().equalsIgnoreCase(applicationStatus)
            );
            if (Objects.nonNull(currApplicationStatus) &&
                    currApplicationStatus.equalsIgnoreCase(MedicalAssistanceStatus.APPROVED.getValue()) &&
                    MemberAccountApplicationStatus.APPROVED.getValue().equalsIgnoreCase(applicationStatus)) {
                resultMap.put("isApproved", true);
                resultMap.put("accountNumber", latestApplication.getMemberAccount().getAccountNumber());
                resultMap.put("voucherNumber", latestApplication.getVoucherNumber());
                resultMap.put("isClaimed", Objects.nonNull(latestApplication.getVoucher())
                        && Objects.nonNull(latestApplication.getVoucher().getStatus())
                        && latestApplication.getVoucher().getStatus().equalsIgnoreCase(VoucherStatus.CLAIMED.getValue())
                        ? latestApplication.getVoucher().getStatus() : null);
            }

            resultMap.put("success", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } catch (MemberAssistanceException mae) {
            resultMap.put("error", mae.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultMap.put("error", "Failed to view medical assistance.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/save")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> save(
            @RequestBody MedicalAssistanceApplicationDto medicalAssistanceApplicationDto
    ) {
        LOGGER.info("Save medical assistance application.");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, Object> userInfo = userAuthController.getUserInfo();
            final String loggedUser = (String) userInfo.get("username");
            MemberAccount account = memberService.getUsernameFromUserInfo(loggedUser);
            if (Objects.isNull(account)) {
                throw new MemberAssistanceException("Failed to save application. Cannot retrieve member account.");
            }
            medicalAssistanceService.saveMedicalAssistanceApplication(account, medicalAssistanceApplicationDto);
            resultMap.put("success", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } catch (MemberAssistanceException mae) {
            LOGGER.error(mae.getMessage(), mae);
            resultMap.put("error", mae.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultMap.put("error", "System error to view member assistance.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
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

    @RequestMapping(method = RequestMethod.POST, value = "/claim-voucher/")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> claimVoucher(
    ) {
        LOGGER.info("Claim voucher...");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, Object> userInfo = userAuthController.getUserInfo();
            final String loggedUser = (String) userInfo.get("username");
            MemberAccount account = memberService.getUsernameFromUserInfo(loggedUser);
            if (Objects.isNull(account)) {
                throw new MemberAssistanceException("Failed to claim voucher. Cannot retrieve member account.");
            }

            medicalAssistanceService.claimVoucher(account);
            resultMap.put("success", "Voucher is now claimed.");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } catch (MemberAssistanceException mae) {
            LOGGER.error(mae.getMessage(), mae);
            resultMap.put("error", mae.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultMap.put("error", "Failed to claim voucher.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        }
    }
}
