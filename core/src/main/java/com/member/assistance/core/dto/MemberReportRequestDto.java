package com.member.assistance.core.dto;

public class MemberReportRequestDto {
    private String type;
    private String sName;
    private String sLocation;
    private String sDateOfBirth;
    private String sGender;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsLocation() {
        return sLocation;
    }

    public void setsLocation(String sLocation) {
        this.sLocation = sLocation;
    }

    public String getsDateOfBirth() {
        return sDateOfBirth;
    }

    public void setsDateOfBirth(String sDateOfBirth) {
        this.sDateOfBirth = sDateOfBirth;
    }

    public String getsGender() {
        return sGender;
    }

    public void setsGender(String sGender) {
        this.sGender = sGender;
    }
}
