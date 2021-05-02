package com.member.assistance.core.service;

import com.member.assistance.backend.model.MedicalAssistance;
import com.member.assistance.backend.model.MemberAccount;
import com.member.assistance.core.dto.MedicalAssistanceApplicationDto;
import com.member.assistance.core.dto.MemberApplicationDto;
import com.member.assistance.core.dto.MemberListingDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public interface MedicalAssistanceService {
    List<MedicalAssistance> getMedicalAssistanceByMember(MemberAccount memberAccount, Integer pageIndex, Integer pageSize)
            throws MemberAssistanceException;

    Map<String, Object> getLookupConfigurations()
            throws MemberAssistanceException;

    String getMedicalApplicationStatus(MemberAccount memberAccount);

    Boolean allowMedicalAssistance(String applicationStatus);

    Set<String> getAvailableHospitals();

    Boolean saveMedicalAssistanceApplication(MemberAccount account, MedicalAssistanceApplicationDto medicalAssistanceApplicationDto) throws MemberAssistanceException;

    MemberListingDto denyApplication(MemberListingDto memberListingDto) throws MemberAssistanceException;

    MemberApplicationDto approveApplication(MemberApplicationDto memberApplicationDto) throws MemberAssistanceException;

    MemberApplicationDto getApplication(String accountNumber) throws MemberAssistanceException;

    List<String> getRequirementsList();

    void claimVoucher(MemberAccount account) throws MemberAssistanceException;
}
