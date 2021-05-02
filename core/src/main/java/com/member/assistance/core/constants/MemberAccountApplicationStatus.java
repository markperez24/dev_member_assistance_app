package com.member.assistance.core.constants;

public enum MemberAccountApplicationStatus {
    IN_REVIEW("In-Review"),
    ALLOWED("Allowed"),
    DENIED("Denied"),
    PROCESSING("Processing"),
    APPROVED("Approved");

    private String value;

    MemberAccountApplicationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
