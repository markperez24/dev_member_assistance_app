package com.member.assistance.backend.repository;

import java.util.Date;

import com.member.assistance.backend.model.Hospital;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    Hospital findByHospitalName(String hospitalName);

    @Query(
            "SELECT h                                                                                       " +
            "         FROM Hospital h                                                                         " +
            " WHERE (h.hospitalName LIKE CONCAT(:sName, '%')      " +
            "        OR :sName IS NULL)                                                                    " +
            " AND (h.city LIKE CONCAT(:sLocation, '%') OR h.region LIKE CONCAT(:sLocation, '%')         " +
            "      OR :sLocation IS NULL)                                                                   " +
            " AND (function('DATE_FORMAT',h.createdDate, '%Y-%m-%d') >= function('DATE_FORMAT',:sDateFrom, '%Y-%m-%d')  " +
            "      OR :sDateFrom IS NULL)                                                                " +
            " AND (function('DATE_FORMAT',h.createdDate, '%Y-%m-%d') <= function('DATE_FORMAT',:sDateTo, '%Y-%m-%d')    " +
            "      OR :sDateTo IS NULL)                                                                "
    )
    Page<Hospital> getHospitalByNameAndLocationAndItemAndDate(
            @Param("sName") String sName,
            @Param("sLocation") String sLocation,
            @Param("sDateFrom") Date sDateFrom,
            @Param("sDateTo") Date sDateTo,
            Pageable pageable);

    @Query(
            "SELECT h                                                                                       " +
            "         FROM Hospital h                                                                         " +
            " WHERE (h.hospitalName LIKE CONCAT(:searchField, '%')      " +
            "        OR :searchField IS NULL)                                                                "
    )
    Page<Hospital> findBySearchHospitalName(
            @Param("searchField") String searchField,
            Pageable pageable);

}
