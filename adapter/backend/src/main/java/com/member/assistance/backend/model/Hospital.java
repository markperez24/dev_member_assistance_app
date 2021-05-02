package com.member.assistance.backend.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "ma_hospital")
public class Hospital extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "hospital_name")
    private String hospitalName;
    @Column(name = "address_1")
    private String address1;
    @Column(name = "address_2")
    private String address2;
    private String city;
    private String region;
    private String country;
    
    @OneToMany(mappedBy="hospital", cascade = CascadeType.ALL)
    private Set<HospitalContact> contactSet;

    @OneToOne(mappedBy="hospital", cascade = CascadeType.ALL)
    private HospitalAccount account;

    @OneToMany(mappedBy="hospital", cascade = CascadeType.ALL)
    private Set<HospitalTransaction> transactionSet;

    public Hospital() {
    }

    public Hospital(Long id,
                    String hospitalName,
                    String address1,
                    String address2,
                    String city,
                    String region,
                    String country) {
        this.id = id;
        this.hospitalName = hospitalName;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Set<HospitalContact> getContactSet() {
        return contactSet;
    }

    public void setContactSet(Set<HospitalContact> contactSet) {
        this.contactSet = contactSet;
    }

    public HospitalAccount getAccount() {
        return account;
    }

    public void setAccount(HospitalAccount account) {
        this.account = account;
    }

    public Set<HospitalTransaction> getTransactionSet() {
        return transactionSet;
    }

    public void setTransactionSet(Set<HospitalTransaction> transactionSet) {
        this.transactionSet = transactionSet;
    }

}
