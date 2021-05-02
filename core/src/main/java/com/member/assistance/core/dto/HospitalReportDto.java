package com.member.assistance.core.dto;

import com.member.assistance.backend.model.Hospital;

import java.math.BigDecimal;
import java.util.Date;

public class HospitalReportDto {
    private String accountNumber;
    private String hospitalName;
    private String location;
    private Date dateAdded;
    private BigDecimal item;
    private String itemName;

    public HospitalReportDto() {
    }

    public HospitalReportDto(Hospital hospital) {
        this.setHospitalName(hospital.getHospitalName());
        this.setDateAdded(hospital.getCreatedDate());
        this.setItem(hospital.getAccount().getBudget()
            .subtract(hospital.getAccount().getBalance()));
        this.setLocation(hospital.getCity()
                .concat(", ")
                .concat(hospital.getRegion()));
    }

    public HospitalReportDto(Hospital hospital, String item) {
        this.setHospitalName(hospital.getHospitalName());
        this.setDateAdded(hospital.getCreatedDate());
        BigDecimal amount = new BigDecimal(0);
        if (item.equalsIgnoreCase("Balance")) {
            amount = hospital.getAccount().getBalance();
        } else if (item.equalsIgnoreCase("Budget")) {
            amount = hospital.getAccount().getBudget();
        }
        this.setItem(amount);
        this.setItemName(item);
        this.setLocation(hospital.getCity()
                .concat(", ")
                .concat(hospital.getRegion()));
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public BigDecimal getItem() {
        return item;
    }

    public void setItem(BigDecimal item) {
        this.item = item;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
}
