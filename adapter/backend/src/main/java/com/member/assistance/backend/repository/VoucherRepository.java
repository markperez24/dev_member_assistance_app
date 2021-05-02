package com.member.assistance.backend.repository;

import com.member.assistance.backend.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Voucher findByVoucherUuid(String voucherUuid);

    Voucher findByHospitalNameAndVoucherNumber(String hospitalName, String voucherNumber);
}
