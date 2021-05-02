package com.member.assistance.core.service;

public interface ReportsGenerationFactory {
    ReportsGenerationService getReportsGenerationServiceImpl(String reportType);
}
