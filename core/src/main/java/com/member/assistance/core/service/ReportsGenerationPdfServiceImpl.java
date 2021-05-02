package com.member.assistance.core.service;

import com.member.assistance.core.constants.ReportFileType;
import com.member.assistance.core.dto.HospitalReportDto;
import com.member.assistance.core.dto.MemberReportDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service(ReportFileType.ReportTypeContants.PDF)
public class ReportsGenerationPdfServiceImpl implements ReportsGenerationService {
    @Override
    public ReportsGenerationService generate(String reportFileType) {
        return null;
    }

    @Override
    public void generateMemberReports(List<MemberReportDto> memberList, boolean eof) {
    }

    @Override
    public void generateHospitalReports(List<HospitalReportDto> hospitalList, boolean eof, String itemName) {
    }

    @Override
    public ReportsGenerationService export(String reportFileType) {
        return null;
    }

    @Override
    public Map<String, Object> exportMemberReports() {
        return null;
    }

    @Override
    public Map<String, Object> exportHospitalReports() throws MemberAssistanceException {
        return null;
    }
}
