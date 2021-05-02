package com.member.assistance.core.service;

import com.member.assistance.core.dto.MemberApplicationDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import org.springframework.stereotype.Service;

@Service
public interface VoucherHandlingService {
    String generateQRCodeImage(MemberApplicationDto memberApplicationDto)
            throws MemberAssistanceException;

    Byte[] getQRCodeImage(

    ) throws MemberAssistanceException;

}
