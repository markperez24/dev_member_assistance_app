package com.member.assistance.core.dto;

import com.member.assistance.backend.model.Member;

import java.util.Date;

public class MemberReportDto {
    private String accountNumber;
    private String fullName;
    private String location;
    private Date dateOfBirth;
    private String gender;
    private Integer age;

    public MemberReportDto() {
    }

    public MemberReportDto(Member member) {
        //MemberReportDto m = new MemberReportDto();
        this.setFullName(member.getFirstName()
                .concat(" ").concat(member.getMiddleName())
                .concat(" ").concat(member.getLastName()));
        this.setAge(member.getAge());
        this.setDateOfBirth(member.getDateOfBirth());
        this.setGender(member.getGender());
        this.setLocation(member.getCity()
                .concat(", ")
                .concat(member.getProvince()));
    }

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
