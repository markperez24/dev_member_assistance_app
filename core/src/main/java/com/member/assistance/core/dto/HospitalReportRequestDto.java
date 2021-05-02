package com.member.assistance.core.dto;

import java.util.Date;

public class HospitalReportRequestDto {
    private String type;
    private String sName;
    private String sLocation;
    private Date sDateFrom;
    private Date sDateTo;
    private String sItem;

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

    public Date getsDateFrom() {
        return sDateFrom;
    }

    public void setsDateFrom(Date sDateFrom) {
        this.sDateFrom = sDateFrom;
    }

    public Date getsDateTo() {
        return sDateTo;
    }

    public void setsDateTo(Date sDateTo) {
        this.sDateTo = sDateTo;
    }

    public String getsItem() {
        return sItem;
    }

    public void setsItem(String sItem) {
        this.sItem = sItem;
    }
}
