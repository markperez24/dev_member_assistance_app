package com.member.assistance.backend.repository;

import com.member.assistance.backend.model.HospitalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalTransactionRepository extends JpaRepository<HospitalTransaction, Long> {
}
