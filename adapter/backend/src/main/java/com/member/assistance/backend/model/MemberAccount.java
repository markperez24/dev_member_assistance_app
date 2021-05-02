package com.member.assistance.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="ma_member_account")
public class MemberAccount extends AuditEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="email_address")
    private String emailAddress;
    @Column(name="account_number")
    private String accountNumber;
    @Column(name="profile_photo_uuid")
    private String profilePhotoUuid;
    @Column(name="id_photo_uuid")
    private String idPhotoUuid;
    @Column(name="account_status")
    private String accountStatus;
    @Column(name="application_status")
    private String applicationStatus;
    @Column(name="last_applied_date")
    private Date lastAppliedDate;
    @Column(name="total_assistance_amount")
    private String totalAssistanceAmount;

    @OneToOne
    //@JoinColumn(name="email_address", updatable = false, nullable = false, referencedColumnName="email_address")
    @JoinColumn(name="member_id", updatable = false, nullable = false)
    @JsonIgnore
    private Member member;

    @OneToMany(mappedBy = "memberAccount")
    private Set<MedicalAssistance> medicalAssistanceSet;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberAccount account = (MemberAccount) o;
        return emailAddress.equals(account.emailAddress) &&
                accountNumber.equals(account.accountNumber);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Date getLastAppliedDate() {
        return lastAppliedDate;
    }

    public void setLastAppliedDate(Date lastAppliedDate) {
        this.lastAppliedDate = lastAppliedDate;
    }

    public String getTotalAssistanceAmount() {
        return totalAssistanceAmount;
    }

    public void setTotalAssistanceAmount(String totalAssistanceAmount) {
        this.totalAssistanceAmount = totalAssistanceAmount;
    }

    public Set<MedicalAssistance> getMedicalAssistanceSet() {
        return medicalAssistanceSet;
    }

    public void setMedicalAssistanceSet(Set<MedicalAssistance> medicalAssistanceSet) {
        this.medicalAssistanceSet = medicalAssistanceSet;
    }

    public String getProfilePhotoUuid() {
        return profilePhotoUuid;
    }

    public void setProfilePhotoUuid(String profilePhotoUuid) {
        this.profilePhotoUuid = profilePhotoUuid;
    }

    public String getIdPhotoUuid() {
        return idPhotoUuid;
    }

    public void setIdPhotoUuid(String idPhotoUuid) {
        this.idPhotoUuid = idPhotoUuid;
    }
}
