package com.member.assistance.backend.repository;

import com.member.assistance.backend.model.MedicalAssistance;
import com.member.assistance.backend.model.MemberAccount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalAssistanceRepository extends JpaRepository<MedicalAssistance, Long> {
    List<MedicalAssistance> findByMemberAccountOrderByDateAppliedDesc(
            MemberAccount memberAccount,
            Pageable pageable
    );

    List<MedicalAssistance> findByMemberAccount(MemberAccount memberAccount, Pageable pageable);

    List<MedicalAssistance> findByMemberAccountAndStatusIn(MemberAccount memberAccount, List<String> statuses);

    List<MedicalAssistance> findByHospitalName(String hospitalName, Pageable pageable);

    @Query(
            "SELECT COUNT(*)                                                                                       " +
            "         FROM MedicalAssistance ma                                                                         " +
            " WHERE ( ma.status LIKE CONCAT(:sStatus, '%'))"
    )
    Integer getTotalAssistedCount(@Param("sStatus") String sStatus); 
}

