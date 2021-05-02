package com.member.assistance.backend.repository;

import com.member.assistance.backend.model.HospitalAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalAccountRepository extends JpaRepository<HospitalAccount, Long> {
}
