package com.member.assistance.core.dto;

import java.math.BigDecimal;

public class HospitalDto {
    private String hospitalName;
    private String location;

    private BigDecimal budget;
    private BigDecimal balance;

    private BigDecimal totalBudget;
    private BigDecimal totalBalance;
    private Integer totalAssistedCount;
    

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

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Integer getTotalAssistedCount() {
        return totalAssistedCount;
    }

    public void setTotalAssistedCount(Integer totalAssistedCount) {
        this.totalAssistedCount = totalAssistedCount;
    }
}
