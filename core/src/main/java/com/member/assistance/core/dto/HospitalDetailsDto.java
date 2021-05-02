package com.member.assistance.core.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class HospitalDetailsDto {
    private Long hospitalId;
    private String hospitalName;
    private String location;
    private String contactPerson;
    private String designation;
    private String contactNumber;
    private String emailAddress;
    
    private BigDecimal budget;
    private BigDecimal balance;
    private Integer totalAssistedCount;

    private String firstName;
    private String lastName;
    private String middleName;
    private Date dateAwarded;
    private String voucherNumber;
    private BigDecimal amount;
    private String memberId;
    private Date dateCreated;

    // Medical Assistance Summary
    private List<MedicalAssistanceDetailsDto> medicalAssistanceList;
    
    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }    

    public Integer getTotalAssistedCount() {
        return totalAssistedCount;
    }

    public void setTotalAssistedCount(Integer totalAssistedCount) {
        this.totalAssistedCount = totalAssistedCount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Date getDateAwarded() {
        return dateAwarded;
    }

    public void setDateAwarded(Date dateAwarded) {
        this.dateAwarded = dateAwarded;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<MedicalAssistanceDetailsDto> getMedicalAssistanceList() {
        return medicalAssistanceList;
    }

    public void setMedicalAssistanceList(List<MedicalAssistanceDetailsDto> medicalAssistanceList) {
        this.medicalAssistanceList = medicalAssistanceList;
    }

}
