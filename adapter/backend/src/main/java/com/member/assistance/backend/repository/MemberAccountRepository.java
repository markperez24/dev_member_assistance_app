package com.member.assistance.backend.repository;

import com.member.assistance.backend.model.Member;
import com.member.assistance.backend.model.MemberAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberAccountRepository extends JpaRepository<MemberAccount, Long> {
    MemberAccount findByMember(Member member);
    MemberAccount findByEmailAddress(String emailAddress);
    MemberAccount findByAccountNumber(String accountNumber);

    @Query(
            "SELECT ma                                                                                      " +
            "         FROM MemberAccount ma                                                                 " +
            "         JOIN Member m                                                                         " +
            "             ON ma.member = m.id                                                               " +
            " WHERE (ma.accountNumber LIKE CONCAT(:searchField , '%')                                       " +
            "     OR :searchField IS NULL)                                                                  " +
            " OR (                                                                                          " +
            "        (CONCAT(m.firstName, ' ', m.middleName, ' ', m.lastName) LIKE CONCAT(:searchField, '%')" +
            "        OR m.firstName LIKE CONCAT(:searchField, '%')                                          " +
            "        OR m.lastName LIKE CONCAT(:searchField, '%')                                           " +
            "        OR m.middleName LIKE CONCAT(:searchField, '%'))                                        " +
            "           OR :searchField IS NULL )                                                           " +
            " AND (ma.applicationStatus = 'Processing'                                                      " +
            "      OR ma.applicationStatus = 'Approved'                                                     " +
            "      OR ma.applicationStatus = 'Denied')                                                      "
    )
    Page<MemberAccount> getByFullNameAndAccountNumber(@Param("searchField") String searchField,
                                                      Pageable pageable);
}
