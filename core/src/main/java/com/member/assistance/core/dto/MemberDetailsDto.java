package com.member.assistance.core.dto;

import java.util.List;

public class MemberDetailsDto {
    private String accountNumber;
    private String fullName;    
    private String fullAddress;
    private String dateOfBirth;
    private String gender;
    private String contactNumber;

    // Medical Assistance Summary
    private List<MedicalAssistanceDetailsDto> medicalAssistanceList;
    
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public List<MedicalAssistanceDetailsDto> getMedicalAssistanceList() {
        return medicalAssistanceList;
    }

    public void setMedicalAssistanceList(List<MedicalAssistanceDetailsDto> medicalAssistanceList) {
        this.medicalAssistanceList = medicalAssistanceList;
    }

}
