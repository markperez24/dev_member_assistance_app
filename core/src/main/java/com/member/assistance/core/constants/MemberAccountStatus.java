package com.member.assistance.core.constants;

public enum MemberAccountStatus {
    IN_REVIEW("In-Review"),
    SUSPENDED("Suspended"),
    VERIFIED("Verified");
    private String value;

    MemberAccountStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
