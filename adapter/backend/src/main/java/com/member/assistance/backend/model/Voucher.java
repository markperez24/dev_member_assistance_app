package com.member.assistance.backend.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "ma_voucher")
public class Voucher extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "patient_full_name")
    private String patientFullName;
    @Column(name="account_number")
    private String accountNumber;
    @Column(name="voucher_number")
    private String voucherNumber;
    @Column(name="hospital_name")
    private String hospitalName;
    private BigDecimal amount;
    @Column(name="voucher_uuid")
    private String voucherUuid;

    private String status;

    @OneToOne
    @JoinColumn(name = "application_id", updatable = false, nullable = false)
    private MedicalAssistance medicalAssistance;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Voucher voucher = (Voucher) o;
        return voucherNumber.equals(voucher.getVoucherNumber()) &&
                voucherUuid.equals(voucher.getVoucherUuid());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientFullName() {
        return patientFullName;
    }

    public void setPatientFullName(String patientFullName) {
        this.patientFullName = patientFullName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getVoucherUuid() {
        return voucherUuid;
    }

    public void setVoucherUuid(String voucherUuid) {
        this.voucherUuid = voucherUuid;
    }

    public MedicalAssistance getMedicalAssistance() {
        return medicalAssistance;
    }

    public void setMedicalAssistance(MedicalAssistance medicalAssistance) {
        this.medicalAssistance = medicalAssistance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
