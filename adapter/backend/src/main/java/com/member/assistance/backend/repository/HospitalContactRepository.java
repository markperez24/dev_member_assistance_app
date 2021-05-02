package com.member.assistance.backend.repository;

import com.member.assistance.backend.model.HospitalContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalContactRepository extends JpaRepository<HospitalContact, Long> {
}
