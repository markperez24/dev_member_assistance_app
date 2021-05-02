package com.member.assistance.core.service;

import com.member.assistance.core.constants.ReportFileType;
import com.member.assistance.core.dto.HospitalReportDto;
import com.member.assistance.core.dto.MemberReportDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportsGenerationServiceImpl implements ReportsGenerationService {
    Logger LOGGER = LogManager.getLogger(ReportsGenerationServiceImpl.class);

    @Autowired
    private ReportsGenerationFactory reportsGenerationFactory;

    @Override
    public ReportsGenerationService generate(String reportFileType) throws MemberAssistanceException {
        LOGGER.info("Generate report with type: {}...", reportFileType);
        try {
            if (!reportFileType.equalsIgnoreCase(ReportFileType.EXCEL.getValue())) {
                throw new MemberAssistanceException("Report type not supported.");
            }
            return reportsGenerationFactory.getReportsGenerationServiceImpl(reportFileType);
        } catch (MemberAssistanceException mae ) {
            throw mae;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Failed to generate reports.");
        }
    }

    @Override
    public void generateMemberReports(List<MemberReportDto> memberList, boolean eof) {
    }

    @Override
    public void generateHospitalReports(List<HospitalReportDto> hospitalList, boolean eof, String itemName) {
    }

    @Override
    public ReportsGenerationService export(String reportFileType) throws MemberAssistanceException {
        LOGGER.info("Download report with type: {}...", reportFileType);
        try {
            if(!reportFileType.equalsIgnoreCase(ReportFileType.EXCEL.getValue())) {
                throw new MemberAssistanceException("Report type not supported.");
            }
            return reportsGenerationFactory.getReportsGenerationServiceImpl(reportFileType);
        } catch (MemberAssistanceException mae ) {
            throw mae;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Failed to download reports.");
        }
    }

    @Override
    public Map<String, Object> exportMemberReports() throws MemberAssistanceException{
        return null;
    }

    @Override
    public Map<String, Object> exportHospitalReports() throws MemberAssistanceException {
        return null;
    }
}
