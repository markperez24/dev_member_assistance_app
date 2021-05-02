package com.member.assistance.core.dto;

import com.member.assistance.backend.model.Member;

import java.util.Date;

public class MemberDto {
    private String accountNumber;
    private String fullName;
    private String lastName;
    private String firstName;
    private String middleName;
    private String emailAddress;
    private String address1;
    private String address2;
    private Date dateOfBirth;
    private String gender;
    private String contactNumber;
    private String city;
    private String province;
    private Integer age;

    public MemberDto toDto(Member member) {
        MemberDto m = new MemberDto();
        m.setFullName(member.getFirstName()
                .concat(" ").concat(member.getMiddleName())
                .concat(" ").concat(member.getLastName()));
        m.setFirstName(member.getFirstName());
        m.setLastName(member.getLastName());
        m.setMiddleName(member.getMiddleName());
        m.setAddress1(member.getAddress1());
        m.setAddress2(member.getAddress2());
        m.setAge(member.getAge());
        m.setDateOfBirth(member.getDateOfBirth());
        m.setCity(member.getCity());
        m.setProvince(member.getProvince());
        m.setContactNumber(member.getContactNumber());
        m.setGender(member.getGender());
        m.setEmailAddress(member.getEmailAddress());
        return m;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
