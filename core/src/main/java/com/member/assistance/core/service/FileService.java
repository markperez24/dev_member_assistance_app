package com.member.assistance.core.service;

import com.google.zxing.common.BitMatrix;
import com.member.assistance.core.dto.MemberApplicationDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

/**
 * Dummy implementation to store in file the photos
 * TODO: will transfer data in cloud storage
 * Reference https://github.com/bezkoder/spring-boot-upload-multiple-files
 */
@Service
public interface FileService {
    void init();
    Resource createUpdateProfilePhoto(String accountNumber,
                                      String profileUrl,
                                      InputStream file,
                                      Boolean isNew) throws MemberAssistanceException;

    Resource validateIfFileExists(Path file);

    ByteArrayResource getProfilePhoto(String accountNumber,
                                      String filename) throws MemberAssistanceException;

    ByteArrayResource getIdPhoto(String accountNumber, String idPhotoUuid, String side) throws MemberAssistanceException;

    ByteArrayResource getVoucherImage(String voucherNumber, String voucherUuid) throws MemberAssistanceException;

    List<Resource> createUpdateIdPhotos(String accountNumber,
                                        String currIdPhotoUuid,
                                        InputStream frontIs,
                                        InputStream backIs,
                                        Boolean isNew) throws MemberAssistanceException;

    void createVoucherFile(MemberApplicationDto memberApplicationDto, String voucherUuid, BitMatrix bitMatrix) throws MemberAssistanceException;
}
