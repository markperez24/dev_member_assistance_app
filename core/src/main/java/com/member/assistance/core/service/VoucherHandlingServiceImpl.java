package com.member.assistance.core.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.member.assistance.backend.model.ConfigParam;
import com.member.assistance.backend.model.Voucher;
import com.member.assistance.backend.repository.MedicalAssistanceRepository;
import com.member.assistance.backend.repository.VoucherRepository;
import com.member.assistance.core.constants.MemberAssistanceConstants;
import com.member.assistance.core.dto.MemberApplicationDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class VoucherHandlingServiceImpl implements VoucherHandlingService {
    private static Logger LOGGER = LogManager.getLogger(VoucherHandlingServiceImpl.class);

    @Autowired
    MedicalAssistanceRepository medicalAssistanceRepository;

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    FileService fileService;

    @Autowired
    ConfigParamService configParamService;

    @Override
    public String generateQRCodeImage(MemberApplicationDto memberApplicationDto)
            throws MemberAssistanceException {
        try {
            LOGGER.info("Generate qr code image for user w/ account#: {}", memberApplicationDto.getAccountNumber());
            final String voucherUuid = UUID.randomUUID().toString();
            Voucher voucher = voucherRepository.findByVoucherUuid(voucherUuid);
            if (Objects.nonNull(voucher)) {
                LOGGER.warn("Voucher uuid exists. regenerate new uuid.");
                //call generate again;
                generateQRCodeImage(memberApplicationDto);
            }

            List<ConfigParam> qrSettings = configParamService.getConfigByType(MemberAssistanceConstants.CONFIG_TYPE_QR_CODE_SETTINGS);
            Integer width = Integer.parseInt(qrSettings.stream().filter(qr ->
                    qr.getCode().equalsIgnoreCase(MemberAssistanceConstants.CONFIG_CODE_QR_CODE_WIDTH))
                    .findFirst().map(ConfigParam::getValue).orElse(MemberAssistanceConstants.QR_CODE_WIDTH));
            Integer height = Integer.parseInt(qrSettings.stream().filter(qr ->
                    qr.getCode().equalsIgnoreCase(MemberAssistanceConstants.CONFIG_CODE_QR_CODE_HEIGHT))
                    .findFirst().map(ConfigParam::getValue).orElse(MemberAssistanceConstants.QR_CODE_HEIGHT));

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            //BitMatrix bitMatrix = qrCodeWriter.encode(generateVoucherText(memberApplicationDto, qrSettings),
            BitMatrix bitMatrix = qrCodeWriter.encode(memberApplicationDto.getVoucherNumber(),
                    BarcodeFormat.QR_CODE,
                    width, height);
            //save qr code.
            fileService.createVoucherFile(memberApplicationDto, voucherUuid, bitMatrix);
            return voucherUuid;
        }catch(MemberAssistanceException mae) {
            throw mae;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Unable to generate barcode");
        }
    }

    //TODO: add encryption if needed

    /**
     * Generates voucher qr text
        format: <siteUrl>/voucher/validate/<account_number>/<voucher_number>
     * @param memberApplicationDto
     * @param qrSettings
     * @return
     * @throws MemberAssistanceException
     */
    private String generateVoucherText(MemberApplicationDto memberApplicationDto, List<ConfigParam> qrSettings) throws MemberAssistanceException {
        LOGGER.info("Generate voucher text");
        try {
            String qrCodeUrl = qrSettings.stream().filter(qr ->
                    qr.getCode().equalsIgnoreCase(MemberAssistanceConstants.CONFIG_CODE_QR_CODE_URL))
                    .findFirst().map(ConfigParam::getValue).orElse(MemberAssistanceConstants.HOME_URL);
            return
                qrCodeUrl.concat("/").concat(memberApplicationDto.getAccountNumber())
                    .concat("/").concat(memberApplicationDto.getVoucherNumber());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Failed to generate voucher.");
        }
    }

    @Override
    public Byte[] getQRCodeImage() {
        return new Byte[0];
    }
}
