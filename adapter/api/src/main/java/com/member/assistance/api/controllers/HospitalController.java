package com.member.assistance.api.controllers;

import com.member.assistance.backend.model.MemberAccount;
import com.member.assistance.backend.repository.HospitalRepository;
import com.member.assistance.core.dto.AddHospitalDto;
import com.member.assistance.core.dto.HospitalDetailsDto;
import com.member.assistance.core.dto.HospitalDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import com.member.assistance.core.service.HospitalService;
import com.member.assistance.core.service.MemberService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin()
@RequestMapping(value = "/api/admin/hospital")
public class HospitalController {
    private static Logger LOGGER = LogManager.getLogger(HospitalController.class);

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalService medicalAssistanceService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserAuthController userAuthController;

    @Autowired
    private HospitalRepository hospitalRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    @ResponseBody
    public ResponseEntity<?> getAllHospitals(
            @RequestParam(value = "searchField", required = false) final String searchField,
            @RequestParam(value = "pageIndex" , required = false) final Integer pageIndex,
            @RequestParam(value = "pageSize" , required = false) final Integer pageSize
    ) {
        LOGGER.info("Display hospitals.");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<HospitalDto> allHospitals = hospitalService.getAllHospitals(
                    searchField, pageIndex, pageSize);
            HospitalDto hospitalSummary = hospitalService.getAllHospitalsSummary(allHospitals);
            resultMap.put("hospitals", allHospitals);
            resultMap.put("summary", hospitalSummary);
            resultMap.put("size", allHospitals.size());
            resultMap.put("success", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } catch (MemberAssistanceException mae) {
            LOGGER.error(mae.getMessage(), mae);
            resultMap.put("error", "Failed to retrieve hospital list.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultMap);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultMap.put("error", "Failed to get all hospitals api.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    @ResponseBody
    public Map<String, Object> register(@RequestBody AddHospitalDto addHospitalDto) {
        LOGGER.info("Register a new partner hospital.");
        Map<String, Object> resultMap = new HashMap<>();
        try {

            Map<String, Object> registeredMap = this.hospitalService.registerHospital(addHospitalDto);
            if (Objects.nonNull(registeredMap)) {
                resultMap.putAll(registeredMap);
                resultMap.put("success", "OK");
            } else {
                resultMap.put("error", "Null exception encountered in adding hospital.");
            }
        } catch (MemberAssistanceException mae) {
            LOGGER.error(mae.getMessage(), mae);
            resultMap.put("error", mae.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultMap.put("error", "Failed to register hospital.");
        }
        return resultMap;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-hospital-details")
    @ResponseBody
    public Map<String, Object> getHospitalDetails(
            @RequestParam(value = "hospital", required = false) final String hospitalName
    ) {
        LOGGER.info("Get details of selected hospital.");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            HospitalDetailsDto details = hospitalService.getSelectedHospitalDetails(hospitalName);
            resultMap.put("hospital", details);
            resultMap.put("success", "OK");
        } catch (MemberAssistanceException mae) {
            LOGGER.error(mae.getMessage(), mae);
            resultMap.put("error", "Failed to retrieve selected hospital details.");
        } catch (Exception e) {
            resultMap.put("error", "Failed to get hospital details api.");
        }
        return resultMap;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/view-by-hospital")
    @ResponseBody
    public Map<String, Object> viewHospitalMedicalAssistance(
            @RequestParam(value = "hospital", required = false) final String hospitalName,
            @RequestParam(value = "pageIndex", required = false) final Integer pageIndex,
            @RequestParam(value = "pageSize", required = false) final Integer pageSize
    ) {
        LOGGER.info("View selected hospital medical assistance details.");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            MemberAccount account = memberService.getUsernameFromUserInfo(userAuthController.getUserInfo());
            /* resultMap.put("medicalAssistanceList", medicalAssistanceService
                    .getMedicalAssistanceByHospital(hospitalName, pageIndex, pageSize)); */
                resultMap.put("hospitalMedicalAssistance", medicalAssistanceService
                    .getMedicalAssistanceByHospital(hospitalName, pageIndex, pageSize));
                    resultMap.put("success", "OK");
        } catch (MemberAssistanceException mae) {
            LOGGER.error(mae.getMessage(), mae);
            resultMap.put("error", mae.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultMap.put("error", "System error to view medical assistance.");
        }
        return resultMap;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/add-budget")
    @ResponseBody
    public Map<String, Object> saveAdditionalBudget(
            @RequestBody HospitalDetailsDto hospitalDetailsDto
    ) {
        LOGGER.info("Save Budget");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, Object> userInfo = userAuthController.getUserInfo();
            final String loggedUser = (String) userInfo.get("username");
            MemberAccount account = memberService.getUsernameFromUserInfo(loggedUser);
            if (Objects.isNull(account)) {
                throw new MemberAssistanceException("Failed to save additional budget. Cannot retrieve member account.");
            }
            hospitalService.saveAdditionalBudget(account, hospitalDetailsDto);
            resultMap.put("success", "OK");
        } catch (MemberAssistanceException mae) {
            LOGGER.error(mae.getMessage(), mae);
            resultMap.put("error", mae.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultMap.put("error", "System error to view member assistance.");
        }
        return resultMap;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/view-budget-history")
    @ResponseBody
    public Map<String, Object> getHospitalBudgetHistory(
            @RequestParam(value = "hospital", required = false) final String hospitalName
    ) {
        LOGGER.info("Get budget replenish history of selected hospital.");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<HospitalDetailsDto> details = hospitalService.getSelectedHospitalBudgetHistory(hospitalName);
            resultMap.put("budgetHistoryList", details);
            resultMap.put("success", "OK");
        } catch (MemberAssistanceException mae) {
            LOGGER.error(mae.getMessage(), mae);
            resultMap.put("error", "Failed to retrieve selected hospital budget history.");
        } catch (Exception e) {
            resultMap.put("error", "Failed to get hospital budget history api.");
        }
        return resultMap;
    }

}
