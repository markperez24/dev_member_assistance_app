package com.member.assistance.core.dto;

import com.member.assistance.backend.model.Member;

import java.util.Date;

public class MemberRegistrationDto {
    private String lastName;
    private String firstName;
    private String middleName;
    private String address1;
    private String address2;
    private Date dateOfBirth;
    private String gender;
    private String contactNumber;

    private String city;
    private String province;
    private Integer age;
    private String captcha;

    private String emailAddress;
    private String password;

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

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Member toModel() {
        Member member = new Member();
        member.setFirstName(this.firstName);
        member.setMiddleName(this.middleName);
        member.setLastName(this.lastName);
        member.setAge(this.age);
        member.setDateOfBirth(this.dateOfBirth);
        member.setAddress1(this.address1);
        member.setAddress2(this.address2);
        member.setCity(this.city);
        member.setProvince(this.province);
        member.setContactNumber(this.contactNumber);
        member.setGender(this.gender);
        member.setEmailAddress(this.emailAddress);
        return member;
    }
}
