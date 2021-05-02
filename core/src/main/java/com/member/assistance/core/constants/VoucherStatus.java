package com.member.assistance.core.constants;

public enum VoucherStatus {
    CLAIMED("Claimed");

    private String value;

    VoucherStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
