package com.member.assistance.core.dto;

import com.member.assistance.backend.model.MedicalAssistance;

import java.math.BigDecimal;

public class MemberApplicationDto {
    private Long medicalAssistanceId;
    private String fullName;
    private String accountNumber;
    private String voucherNumber;
    private String hospitalName;
    private BigDecimal amount;

    public MemberApplicationDto() {
    }

    public MemberApplicationDto(MedicalAssistance medicalAssistance) {
        this.medicalAssistanceId = medicalAssistance.getId();
        this.fullName = medicalAssistance.getFirstName()
                .concat(" ").concat(medicalAssistance.getMiddleName())
                .concat(" ").concat(medicalAssistance.getLastName());
        this.accountNumber = medicalAssistance.getMemberAccount().getAccountNumber();
        this.hospitalName = medicalAssistance.getHospitalName();
    }

    public Long getMedicalAssistanceId() {
        return medicalAssistanceId;
    }

    public void setMedicalAssistanceId(Long medicalAssistanceId) {
        this.medicalAssistanceId = medicalAssistanceId;
    }

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

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
