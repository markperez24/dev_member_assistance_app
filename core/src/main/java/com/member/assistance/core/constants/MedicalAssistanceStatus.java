package com.member.assistance.core.constants;

public enum MedicalAssistanceStatus {
    PROCESSING("Processing"),
    APPROVED("Approved"),
    DENIED("Denied"),
    CLAIMED("Claimed"),
    CLOSED("Closed");

    private String value;

    MedicalAssistanceStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
