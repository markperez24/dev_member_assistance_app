package com.member.assistance.backend.repository;

import com.member.assistance.backend.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmailAddress(String email);

    @Query(
            "SELECT m                                                                                       " +
            "         FROM Member m                                                                         " +
            " WHERE ((CONCAT(m.firstName, ' ', m.middleName, ' ', m.lastName) LIKE CONCAT(:sName, '%')      " +
            "        OR m.firstName LIKE CONCAT(:sName, '%')                                                " +
            "        OR m.lastName LIKE CONCAT(:sName, '%')                                                 " +
            "        OR m.middleName LIKE CONCAT(:sName, '%'))                                              " +
            "        OR :sName IS NULL )                                                                    " +
            " AND (m.city LIKE CONCAT (:sLocation, '%') OR m.province LIKE CONCAT (:sLocation, '%')         " +
            "      OR :sLocation IS NULL)                                                                   " +
            " AND (m.gender = :sGender OR :sGender IS NULL)                                                 " +
            " AND (function('DATE_FORMAT',m.dateOfBirth, '%m/%d/%Y') = :sDateOfBirth                        " +
            "      OR :sDateOfBirth IS NULL)                                                                "
    )
    Page<Member> getMemberByNameAndLocationAndGenderAndDateOfBirth(
            @Param("sName") String sName,
            @Param("sLocation") String sLocation,
            @Param("sGender") String sGender,
            @Param("sDateOfBirth") String sDateOfBirth,
            Pageable pageable);
}
