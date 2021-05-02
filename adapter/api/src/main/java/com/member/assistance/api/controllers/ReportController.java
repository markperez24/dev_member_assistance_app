package com.member.assistance.api.controllers;

import com.member.assistance.api.exception.CustomResponseEntityExceptionHandler;
import com.member.assistance.api.response.ResponseMessageApi;
import com.member.assistance.core.constants.MemberAssistanceConstants;
import com.member.assistance.core.dto.HospitalReportRequestDto;
import com.member.assistance.core.dto.MemberReportRequestDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import com.member.assistance.core.service.ConfigParamService;
import com.member.assistance.core.service.ReportService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@CrossOrigin
@RestController
@RequestMapping(value = "/api/reports/")
public class ReportController {
    private static Logger LOGGER = LogManager.getLogger(ReportController.class);

    @Autowired
    private ConfigParamService configParamService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private CustomResponseEntityExceptionHandler customResponseEntityExceptionHandler;

    @RequestMapping(method = RequestMethod.GET, value = "/get-members")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getMembers(
            @RequestParam(name = "sName", required = false) String sName,
            @RequestParam(name = "sLocation", required = false) String sLocation,
            @RequestParam(name = "sGender", required = false) String sGender,
            @RequestParam(name = "sDateOfBirth", required = false) String sDateOfBirth,
            @RequestParam(name = "pageIndex") Integer pageIndex,
            @RequestParam(name = "pageSize") Integer pageSize
    ) {
        LOGGER.info("Get members...");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap.putAll(reportService.getMembers(
                    sName,
                    sLocation,
                    sGender,
                    sDateOfBirth,
                    pageIndex,
                    pageSize
            ));
            resultMap.put("success", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } catch (MemberAssistanceException mae) {
            resultMap.put("error", mae.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        } catch (Exception e) {
            resultMap.put("error", "Failed to get member listing.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/generate-member-reports")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> generateMemberReports(
            @RequestBody MemberReportRequestDto memberReportRequestDto
    ) {
        LOGGER.info("Generate member reports...");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            reportService.generateMemberReports(memberReportRequestDto);
            resultMap.put("message", "Successfully generated member reports.");
            resultMap.put("success", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } catch (MemberAssistanceException mae) {
            resultMap.put("error", mae.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        } catch (Exception e) {
            resultMap.put("error", "Failed to generate member reports.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/download-member-reports/{type}")
    @ResponseBody
    public ResponseEntity<?> downloadMemberReports(
            @PathVariable("type") String reportType
    ) {
        LOGGER.info("Download member reports...");
        try {
            Map<String, Object> reportMap = reportService.downloadMemberReport(reportType);
            final String filename = (String) reportMap.get("filename");
            final InputStreamResource isr = (InputStreamResource) reportMap.get("file");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.add("Content-Disposition", "attachment; filename=\"" + filename + "\"");

            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return new ResponseEntity<>(isr, headers, HttpStatus.OK);
        } catch (MemberAssistanceException mae) {
            return customResponseEntityExceptionHandler.handleFileHandlingException(mae.getMessage(), mae);
        } catch (Exception e) {
            return customResponseEntityExceptionHandler.handleFileHandlingException("Failed to download member reports.", e);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-hospitals")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getHospitals(
            @RequestParam(name = "sName", required = false) String sName,
            @RequestParam(name = "sLocation", required = false) String sLocation,
            @RequestParam(name = "sItem", required = false) String sItem,
            @RequestParam(name = "sDateFrom", required = false) Date sDateFrom,
            @RequestParam(name = "sDateTo", required = false) Date sDateTo,
            @RequestParam(name = "pageIndex") Integer pageIndex,
            @RequestParam(name = "pageSize") Integer pageSize
    ) {
        LOGGER.info("Get hospitals for reports purposes.");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap.putAll(reportService.getHospitals(
                    sName,
                    sLocation,
                    sItem,
                    sDateFrom,
                    sDateTo,
                    pageIndex,
                    pageSize
            ));
            resultMap.put("success", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } catch (MemberAssistanceException mae) {
            resultMap.put("error", mae.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        } catch (Exception e) {
            resultMap.put("error", "Failed to get member listing.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/generate-hospital-reports")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> generateHospitalReports(
            @RequestBody HospitalReportRequestDto hospitalReportRequestDto
    ) {
        LOGGER.info("Generate hospital reports...");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            reportService.generateHospitalReports(hospitalReportRequestDto);
            resultMap.put("message", "Successfully generated hospital reports.");
            resultMap.put("success", "OK");
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        } catch (MemberAssistanceException mae) {
            resultMap.put("error", mae.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        } catch (Exception e) {
            resultMap.put("error", "Failed to generate hospital reports.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/download-hospital-reports/{type}")
    @ResponseBody
    public ResponseEntity<?> downloadHospitalReports(
            @PathVariable("type") String reportType
    ) {
        LOGGER.info("Download hospital reports...");
        try {
            Map<String, Object> reportMap = reportService.downloadHospitalReport(reportType);
            final String filename = (String) reportMap.get("filename");
            final InputStreamResource isr = (InputStreamResource) reportMap.get("file");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.add("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return new ResponseEntity<>(isr, headers, HttpStatus.OK);
        } catch (MemberAssistanceException mae) {
            return customResponseEntityExceptionHandler.handleFileHandlingException(mae.getMessage(), mae);
        } catch (Exception e) {
            return customResponseEntityExceptionHandler.handleFileHandlingException("Failed to download hospital reports.", e);
        }
    }
}
