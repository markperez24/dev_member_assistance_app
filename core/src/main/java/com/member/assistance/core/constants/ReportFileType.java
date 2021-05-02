package com.member.assistance.core.constants;

public enum ReportFileType {
    PDF(ReportTypeContants.PDF),
    EXCEL(ReportTypeContants.EXCEL),
    CSV(ReportTypeContants.CSV);

    private String value;

    ReportFileType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public interface ReportTypeContants {
        String CSV = "csv";
        String EXCEL = "xlsx";
        String PDF = "pdf";
    }
}
