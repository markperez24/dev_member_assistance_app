package com.member.assistance.core.dto;

import java.math.BigDecimal;

public class AddHospitalDto {
    private String hospitalName;
    private String address1;
    private String address2;
    private String location;
    private String contactPerson;
    private String designation;
    private String contactNumber;
    private String emailAddress;
    private BigDecimal allotedBudget;

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
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

    public BigDecimal getAllotedBudget() {
        return allotedBudget;
    }

    public void setAllotedBudget(BigDecimal allotedBudget) {
        this.allotedBudget = allotedBudget;
    }
    
}