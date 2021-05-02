package com.member.assistance.core.service;

import com.member.assistance.core.dto.HospitalReportDto;
import com.member.assistance.core.dto.MemberReportDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ReportsGenerationService {
    ReportsGenerationService generate(String reportFileType) throws MemberAssistanceException;
    void generateMemberReports(List<MemberReportDto> memberList, boolean eof) throws MemberAssistanceException;
    void generateHospitalReports(List<HospitalReportDto> hospitalList, boolean eof, String itemName) throws MemberAssistanceException;
    ReportsGenerationService export(String reportFileType) throws MemberAssistanceException;
    Map<String, Object> exportMemberReports() throws MemberAssistanceException;
    Map<String, Object> exportHospitalReports() throws MemberAssistanceException;
}
