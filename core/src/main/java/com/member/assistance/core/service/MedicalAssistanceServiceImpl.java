package com.member.assistance.core.service;

import com.member.assistance.backend.model.ConfigParam;
import com.member.assistance.backend.model.Hospital;
import com.member.assistance.backend.model.MedicalAssistance;
import com.member.assistance.backend.model.MemberAccount;
import com.member.assistance.backend.model.Voucher;
import com.member.assistance.backend.repository.MedicalAssistanceRepository;
import com.member.assistance.backend.repository.MemberAccountRepository;
import com.member.assistance.backend.repository.VoucherRepository;
import com.member.assistance.core.constants.MedicalAssistanceStatus;
import com.member.assistance.core.constants.MemberAccountApplicationStatus;
import com.member.assistance.core.constants.MemberAssistanceConstants;
import com.member.assistance.core.constants.VoucherStatus;
import com.member.assistance.core.dto.MedicalAssistanceApplicationDto;
import com.member.assistance.core.dto.MemberApplicationDto;
import com.member.assistance.core.dto.MemberListingDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MedicalAssistanceServiceImpl implements MedicalAssistanceService {
    private static Logger LOGGER = LogManager.getLogger(MedicalAssistanceServiceImpl.class);
    @Autowired
    private ConfigParamService configurationService;

    @Autowired
    private HospitalService hospitalService;


    @Autowired
    private MedicalAssistanceRepository medicalAssistanceRepository;

    @Autowired
    private MemberAccountRepository memberAccountRepository;

    @Autowired
    private VoucherHandlingService voucherHandlingService;

    @Autowired
    private VoucherRepository voucherRepository;

    @Override
    public List<MedicalAssistance> getMedicalAssistanceByMember(MemberAccount memberAccount,
                                                                Integer pageIndex,
                                                                Integer pageSize) throws MemberAssistanceException {
        try {
            // Default sort by {Updated Date DESC, Hospital Name}
            /*final Sort.Order sortByAppliedDate = new Sort.Order(
                    Sort.Direction.DESC,
                    "appliedDate"
            );
            PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by(
                    sortByAppliedDate
            ));*/
            /*MemberAccount memberAccount = memberAccountRepository.findByEmailAddress(emailAddress);
            if(Objects.isNull(memberAccount)) {
                throw new MemberAssistanceException("Unable to retrieve member account.");
            }*/

            Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("dateApplied").descending());
            return medicalAssistanceRepository.findByMemberAccount(
                    memberAccount,
                    pageable
            );
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Unable to retrieve medical assistance");
        }
    }

    @Override
    public Map<String, Object> getLookupConfigurations() throws MemberAssistanceException {
        LOGGER.info("Get lookup configurations");
        Map<String, Object> configMap = new HashMap<>();
        try {
            List<ConfigParam> configList = configurationService
                    .getConfigInTypeList(Arrays.asList(new String[]{
                            MemberAssistanceConstants.TYPE_ASSISTANCE_TYPE,
                            MemberAssistanceConstants.TYPE_DIAGNOSIS,
                            MemberAssistanceConstants.TYPE_MAA_MESSAGES,
                            MemberAssistanceConstants.TYPE_HH_MONTHLY_INCOME,
                    }));

            if (CollectionUtils.isEmpty(configList)) {
                throw new MemberAssistanceException("Unable to get configurations");
            }

            List<ConfigParam> assistanceTypeConfigLst = configList.stream().filter(c -> MemberAssistanceConstants.TYPE_ASSISTANCE_TYPE.equalsIgnoreCase(c.getType()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(assistanceTypeConfigLst)) {
                throw new MemberAssistanceException("Unable to get assistance type configurations.");
            }

            List<ConfigParam> diagnosisConfigList = configList.stream().filter(c -> MemberAssistanceConstants.TYPE_DIAGNOSIS.equalsIgnoreCase(c.getType()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(diagnosisConfigList)) {
                throw new MemberAssistanceException("Unable to get dignosis configurations.");
            }

            List<ConfigParam> messagesConfigList = configList.stream().filter(c ->
                    MemberAssistanceConstants.TYPE_MAA_MESSAGES.equalsIgnoreCase(c.getType()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(diagnosisConfigList)) {
                throw new MemberAssistanceException("Unable to get dignosis configurations.");
            }

            List<ConfigParam> monthlyIncomeConfigList = configList.stream().filter(c ->
                    MemberAssistanceConstants.TYPE_HH_MONTHLY_INCOME.equalsIgnoreCase(c.getType()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(monthlyIncomeConfigList)) {
                throw new MemberAssistanceException("Unable to get monthly income configurations.");
            }

            configMap.put(MemberAssistanceConstants.KEY_ASSISTANCE_TYPE_LIST, assistanceTypeConfigLst
                    .stream().map(ConfigParam::getValue).collect(Collectors.toList()));
            configMap.put(MemberAssistanceConstants.KEY_DIAGNOSIS_LIST, diagnosisConfigList
                    .stream().map(ConfigParam::getValue).collect(Collectors.toList()));
            configMap.put(MemberAssistanceConstants.KEY_MAA_MESSAGES, messagesConfigList
                    .stream().map(ConfigParam::getValue).collect(Collectors.toList()));
            configMap.put(MemberAssistanceConstants.KEY_MONTHLY_INCOME_LIST, monthlyIncomeConfigList
                    .stream().map(ConfigParam::getValue).collect(Collectors.toList()));

            return configMap;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Unable to retrieve medical assistance configurations.");
        }
    }

    @Override
    public String getMedicalApplicationStatus(MemberAccount memberAccount) {
        LOGGER.info("Get medical application status");

        try {
            if (Objects.isNull(memberAccount)) {
                throw new MemberAssistanceException("Unable to retrieve application status.");
            }

            return memberAccount.getApplicationStatus();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Boolean allowMedicalAssistance(String applicationStatus) {
        if (MemberAccountApplicationStatus.ALLOWED.getValue().equalsIgnoreCase(applicationStatus)) {
            return true;
        }
        return false;
    }

    @Override
    public Set<String> getAvailableHospitals() {
        //TODO: get hospitals with available budget
        return hospitalService.getAvailableHospitals();
    }

    @Transactional
    @Override
    public Boolean saveMedicalAssistanceApplication(MemberAccount account,
                                                    MedicalAssistanceApplicationDto dto) throws MemberAssistanceException {
        LOGGER.info("Save medical assistance application for: {}.", account.getEmailAddress());
        try {

            //TODO After 3 months application will be closed.
            List<MedicalAssistance> medicalAssistanceList = medicalAssistanceRepository.findByMemberAccountAndStatusIn(
                    account,
                    Arrays.asList(new String[]{
                            MedicalAssistanceStatus.APPROVED.getValue(),
                            MedicalAssistanceStatus.PROCESSING.getValue(),
                            MedicalAssistanceStatus.CLAIMED.getValue(),
                            MedicalAssistanceStatus.DENIED.getValue(),
                    })
            );
            if (CollectionUtils.isNotEmpty(medicalAssistanceList)) {
                LOGGER.warn("Has existing application.");
                throw new MemberAssistanceException("Has existing application.");
            }

            //account.setApplicationStatus(MemberAccountApplicationStatus.PROCESSING.getValue());

            //add 3000 just to check maximum amount
            Hospital hospital = hospitalService.isHospitalBalanceSufficient(dto.getHospital(), new BigDecimal(3000));

            MedicalAssistance medicalAssistance = new MedicalAssistance();
            medicalAssistance.setPatientEmailAddress(dto.getEmailAddress());
            medicalAssistance.setFirstName(dto.getFirstName());
            medicalAssistance.setLastName(dto.getLastName());
            medicalAssistance.setMiddleName(dto.getMiddleName());
            medicalAssistance.setGender(dto.getGender());
            medicalAssistance.setAge(dto.getAge());
            medicalAssistance.setDateOfBirth(dto.getDateOfBirth());

            medicalAssistance.setAddress1(dto.getAddress1());
            medicalAssistance.setAddress2(dto.getAddress2());
            medicalAssistance.setCity(dto.getCity());
            medicalAssistance.setProvince(dto.getProvince());
            medicalAssistance.setContactNumber(dto.getContactNumber());
            medicalAssistance.setMonthlyHouseHoldIncome(dto.getMonthlyIncome());
            medicalAssistance.setDiagnosis(dto.getDiagnosis());
            medicalAssistance.setAssistanceType(dto.getAssistanceType());

            medicalAssistance.setAmount(BigDecimal.ZERO);
            medicalAssistance.setHospitalName(hospital.getHospitalName());
            medicalAssistance.setHospitalId(hospital.getId());

            Date dateApplied = new Date();
            medicalAssistance.setStatus(MedicalAssistanceStatus.PROCESSING.getValue());
            medicalAssistance.setDateApplied(dateApplied);
            medicalAssistance.setMemberAccount(account);

            MedicalAssistance saved = medicalAssistanceRepository.save(medicalAssistance);
            if (Objects.nonNull(saved)) {
                //Since not yet claimed no update on hospital account balance.
                //HospitalAccount hospitalAccount = hospital.getAccount();
                account.setApplicationStatus(MemberAccountApplicationStatus.PROCESSING.getValue());
                account.setLastAppliedDate(dateApplied);
                memberAccountRepository.save(account);
            }
            return true;
        } catch (MemberAssistanceException mae) {
            throw mae;
        } catch (Exception e) {
            LOGGER.info(e.getMessage(), e);
        }
        return false;
    }


    @Override
    public MemberApplicationDto getApplication(String accountNumber) throws MemberAssistanceException {
        LOGGER.info("Get application for member w/ account#: {}", accountNumber);
        try {
            MemberAccount memberAccount = memberAccountRepository.findByAccountNumber(accountNumber);
            if(Objects.isNull(memberAccount)) {
                throw new MemberAssistanceException("Member is not found.");
            }
            Set<MedicalAssistance> medicalAssistanceSet = memberAccount.getMedicalAssistanceSet();
            if(CollectionUtils.isEmpty(medicalAssistanceSet)) {
                throw new MemberAssistanceException("No active member application found.");
            }
            MedicalAssistance medicalAssistance = medicalAssistanceSet.stream().max(Comparator.comparing(MedicalAssistance::getId)).get();
            String status = medicalAssistance.getStatus();
            if(!status.equalsIgnoreCase(MedicalAssistanceStatus.PROCESSING.getValue())) {
                throw new MemberAssistanceException("Cannot process. Current active application status is: " + status);
            }

            MemberApplicationDto memberApplicationDto = new MemberApplicationDto(medicalAssistance);
            return memberApplicationDto;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Unable to retrieve medical application.");
        }
    }

    @Override
    public List<String> getRequirementsList() {
        List<ConfigParam> configByType = configurationService.getConfigByType(
                MemberAssistanceConstants.CONFIG_TYPE_MEMBER_ACCOUNT_REQUIREMENTS);

        if(CollectionUtils.isNotEmpty(configByType)) {
            return configByType.stream().map(ConfigParam::getValue)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Transactional
    @Override
    public MemberListingDto denyApplication(MemberListingDto memberListingDto) throws MemberAssistanceException {
        final String accountNumber = memberListingDto.getAccountNumber();
        LOGGER.info("Deny application for member w/ account#: {}", accountNumber);
        try {
            MemberAccount memberAccount = memberAccountRepository.findByAccountNumber(accountNumber);
            if (Objects.isNull(memberAccount)) {
                throw new MemberAssistanceException("Unable to deny application. Member account missing.");
            }

            Set<MedicalAssistance> medicalAssistanceSet = memberAccount.getMedicalAssistanceSet();
            MedicalAssistance latestMedicalAssistance = medicalAssistanceSet.stream().max(Comparator.comparing(MedicalAssistance::getId))
                    .orElse(null);
            if (Objects.isNull(latestMedicalAssistance)) {
                throw new MemberAssistanceException("No active application.");
            }
            //ma status = new and member account status = processing
            if (latestMedicalAssistance.getStatus().equals(MedicalAssistanceStatus.PROCESSING.getValue())
                    && memberAccount.getApplicationStatus().equals(MemberAccountApplicationStatus.PROCESSING.getValue())
            ) {
                memberAccount.setApplicationStatus(MedicalAssistanceStatus.DENIED.getValue());
                latestMedicalAssistance.setStatus(MedicalAssistanceStatus.DENIED.getValue());
                latestMedicalAssistance.setReason(memberListingDto.getReason());
                medicalAssistanceRepository.save(latestMedicalAssistance);
                MemberAccount updatedMemberAccount = memberAccountRepository.save(memberAccount);
                memberListingDto.setStatus(updatedMemberAccount.getApplicationStatus());
                return memberListingDto;
            } else {
                throw new MemberAssistanceException("Cannot deny/reject current application. Current status: " + latestMedicalAssistance.getStatus());
            }
        } catch (Exception e) {
            throw new MemberAssistanceException("Unable to deny application.");
        }
    }

    @Transactional
    @Override
    public MemberApplicationDto approveApplication(MemberApplicationDto memberApplicationDto) throws MemberAssistanceException {
        final String accountNumber = memberApplicationDto.getAccountNumber();
        LOGGER.info("Approve application for member w/ account#: {}", accountNumber);
        try {
            MemberAccount memberAccount = memberAccountRepository.findByAccountNumber(accountNumber);
            if (Objects.isNull(memberAccount)) {
                throw new MemberAssistanceException("Unable to approve application. Member account missing.");
            }

            Set<MedicalAssistance> medicalAssistanceSet = memberAccount.getMedicalAssistanceSet();
            MedicalAssistance latestMedicalAssistance = medicalAssistanceSet.stream().max(Comparator.comparing(MedicalAssistance::getId))
                    .orElse(null);
            if (Objects.isNull(latestMedicalAssistance)) {
                throw new MemberAssistanceException("No active application.");
            }

            if (!memberApplicationDto.getMedicalAssistanceId().equals(latestMedicalAssistance.getId())) {
                throw new MemberAssistanceException("Mismatch approved application id.");
            }

            //ma status = new and member account status = processing
            if (latestMedicalAssistance.getStatus().equals(MedicalAssistanceStatus.PROCESSING.getValue())
                    && memberAccount.getApplicationStatus().equals(MemberAccountApplicationStatus.PROCESSING.getValue())
            ) {

                Voucher existingVoucher = voucherRepository.findByHospitalNameAndVoucherNumber(
                        memberApplicationDto.getHospitalName(),
                        memberApplicationDto.getVoucherNumber()
                );

                if(Objects.nonNull(existingVoucher)) {
                    throw new MemberAssistanceException("Voucher already assigned to an existing application.");
                }

                //create voucher file
                String voucherUuid = voucherHandlingService.generateQRCodeImage(memberApplicationDto);
                Voucher voucher = new Voucher();
                voucher.setVoucherUuid(voucherUuid);
                voucher.setPatientFullName(memberApplicationDto.getFullName());
                voucher.setAmount(memberApplicationDto.getAmount());
                voucher.setHospitalName(memberApplicationDto.getHospitalName());
                voucher.setMedicalAssistance(latestMedicalAssistance);
                voucher.setVoucherNumber(memberApplicationDto.getVoucherNumber());
                voucher.setAccountNumber(accountNumber);
                Voucher savedVoucher = voucherRepository.save(voucher);

                latestMedicalAssistance.setDateAwarded(new Date());
                latestMedicalAssistance.setVoucherNumber(memberApplicationDto.getVoucherNumber());
                latestMedicalAssistance.setAmount(memberApplicationDto.getAmount());
                latestMedicalAssistance.setHospitalName(memberApplicationDto.getHospitalName());
                latestMedicalAssistance.setStatus(MedicalAssistanceStatus.APPROVED.getValue());
                latestMedicalAssistance.setVoucher(savedVoucher);
                medicalAssistanceRepository.save(latestMedicalAssistance);
                memberAccount.setApplicationStatus(MedicalAssistanceStatus.APPROVED.getValue());
                memberAccountRepository.save(memberAccount);

                hospitalService.deductVoucherAmount(latestMedicalAssistance);

                return memberApplicationDto;
            } else {
                throw new MemberAssistanceException("Cannot approve current application. Current status: " + latestMedicalAssistance.getStatus());
            }
        } catch (MemberAssistanceException mae) {
            throw mae;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Unable to approve application.");
        }
    }

    @Override
    public void claimVoucher(MemberAccount account) throws MemberAssistanceException {
        try {
            Set<MedicalAssistance> medicalAssistanceSet = account.getMedicalAssistanceSet();
            MedicalAssistance latestMedicalAssistance = medicalAssistanceSet.stream().max(Comparator.comparing(MedicalAssistance::getId))
                    .orElse(null);
            if (Objects.isNull(latestMedicalAssistance)) {
                throw new MemberAssistanceException("No active application.");
            }

            Voucher voucher = latestMedicalAssistance.getVoucher();
            voucher.setStatus(VoucherStatus.CLAIMED.getValue());
            voucherRepository.save(voucher);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new  MemberAssistanceException("Failed to claim voucher.");
        }
    }
}
