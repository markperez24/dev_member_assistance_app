package com.member.assistance.core.service;

import com.member.assistance.backend.model.MedicalAssistance;
import com.member.assistance.core.dto.AddHospitalDto;
import com.member.assistance.core.dto.HospitalDetailsDto;
import com.member.assistance.backend.model.Hospital;
import com.member.assistance.backend.model.MemberAccount;
import com.member.assistance.core.dto.HospitalDto;
import com.member.assistance.core.exception.MemberAssistanceException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface HospitalService {
    /**
     * Get all hospitals
     * @return
     * @param searchField
     * @param pageIndex
     * @param pageSize
     */
    List<HospitalDto> getAllHospitals(String searchField, Integer pageIndex, Integer pageSize) throws MemberAssistanceException;
    
    /**
     * Register/Add a new partner hospital
     */
    Map<String, Object> registerHospital(AddHospitalDto addHospitalDto) throws MemberAssistanceException;
    

    Set<String> getAvailableHospitals();

    Hospital isHospitalBalanceSufficient(String hospitalName, BigDecimal amount) throws MemberAssistanceException;

    HospitalDetailsDto getSelectedHospitalDetails(String hospitalName) throws MemberAssistanceException;

    /* List<MedicalAssistance> getMedicalAssistanceByHospital(String hospitalName, Integer pageIndex, Integer pageSize)
            throws MemberAssistanceException; */
    
    HospitalDetailsDto getMedicalAssistanceByHospital(String hospitalName, Integer pageIndex, Integer pageSize)
            throws MemberAssistanceException;

    Boolean saveAdditionalBudget(MemberAccount account, HospitalDetailsDto hospitalDetailsDto) 
            throws MemberAssistanceException;

    List<HospitalDetailsDto> getSelectedHospitalBudgetHistory(String hospitalName) throws MemberAssistanceException;

    HospitalDto getAllHospitalsSummary(List<HospitalDto> hospitalList) throws MemberAssistanceException;

    void deductVoucherAmount(MedicalAssistance latestMedicalAssistance);
}
