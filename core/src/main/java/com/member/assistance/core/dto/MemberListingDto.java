package com.member.assistance.core.dto;

public class MemberListingDto {
    private String fullName;
    private String accountNumber;
    private Integer totalApplication;
    private String status;
    private String reason;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getTotalApplication() {
        return totalApplication;
    }

    public void setTotalApplication(Integer totalApplication) {
        this.totalApplication = totalApplication;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
