package com.member.assistance.core.service;

import com.member.assistance.core.dto.HospitalReportRequestDto;
import com.member.assistance.core.dto.MemberReportRequestDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public interface ReportService {
    /**
     * List members on queried params for reporting
     * @param sName
     * @param sLocation
     * @param sGender
     * @param sDateOfBirth
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws MemberAssistanceException
     */
    Map<String, Object> getMembers(String sName,
                                   String sLocation,
                                   String sGender,
                                   String sDateOfBirth,
                                   Integer pageIndex,
                                   Integer pageSize) throws MemberAssistanceException;

    /**
     * Generate reports based on filtered params.
     * @param memberReportRequestDto
     */
    void generateMemberReports(MemberReportRequestDto memberReportRequestDto)
            throws MemberAssistanceException;
    
    
    Map<String, Object> getHospitals(String sName,
                                   String sLocation,
                                   String sItem,
                                   Date sDateFrom,
                                   Date sDateTo,
                                   Integer pageIndex,
                                   Integer pageSize) throws MemberAssistanceException;

    void generateHospitalReports(HospitalReportRequestDto hospitalReportRequestDto)
            throws MemberAssistanceException;

    Map<String, Object> downloadMemberReport(String reportType) throws MemberAssistanceException;

    Map<String, Object> downloadHospitalReport(String reportType) throws MemberAssistanceException;
}
