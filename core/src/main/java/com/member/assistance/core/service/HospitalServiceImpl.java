package com.member.assistance.core.service;

import com.member.assistance.backend.model.Hospital;
import com.member.assistance.backend.model.HospitalAccount;
import com.member.assistance.backend.model.HospitalContact;
import com.member.assistance.backend.model.HospitalTransaction;
import com.member.assistance.backend.model.MedicalAssistance;
import com.member.assistance.backend.model.MemberAccount;
import com.member.assistance.backend.repository.HospitalAccountRepository;
import com.member.assistance.backend.repository.HospitalContactRepository;
import com.member.assistance.backend.repository.HospitalRepository;
import com.member.assistance.backend.repository.HospitalTransactionRepository;
import com.member.assistance.backend.repository.MedicalAssistanceRepository;
import com.member.assistance.core.constants.MedicalAssistanceStatus;
import com.member.assistance.core.dto.AddHospitalDto;
import com.member.assistance.core.dto.HospitalDetailsDto;
import com.member.assistance.core.dto.HospitalDto;
import com.member.assistance.core.dto.MedicalAssistanceDetailsDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HospitalServiceImpl implements HospitalService {
    private static Logger LOGGER = LogManager.getLogger(HospitalServiceImpl.class);

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private HospitalTransactionRepository hospitalTransactionRepository;

    @Autowired
    private MedicalAssistanceRepository medicalAssistanceRepository;

    @Autowired
    private HospitalAccountRepository hospitalAccountRepository;

    @Override
    public List<HospitalDto> getAllHospitals(String searchField, 
                                             Integer pageIndex, 
                                             Integer pageSize) throws MemberAssistanceException {
        LOGGER.info("Get all hospitals or hospital(s) by name: {}", searchField);
        try {
            // Default sort by {Updated Date DESC, Hospital Name}
            final Sort.Order sortByHospitalName = new Sort.Order(Sort.Direction.ASC, "hospitalName");
            // Default sort by {Updated Date DESC, Hospital Name}
            final Sort.Order sortByUpdatedDate = new Sort.Order(Sort.Direction.DESC, "updatedDate");
            PageRequest pageRequest = PageRequest.of(pageIndex, pageSize,
                    Sort.by(sortByHospitalName, sortByUpdatedDate));
            //Page<Hospital> hospitals = hospitalRepository.findAll(pageRequest);
            Page<Hospital> hospitals = hospitalRepository.findBySearchHospitalName(searchField, pageRequest);

            if (Objects.nonNull(hospitals)) {
                List<HospitalDto> hospitalDtoList = new ArrayList<>();
                for (Hospital hospital : hospitals.getContent()) {
                    HospitalDto hospitalDto = new HospitalDto();
                    hospitalDto.setHospitalName(hospital.getHospitalName());
                    hospitalDto.setLocation(hospital.getCity().concat(", ").concat(hospital.getRegion()));

                    HospitalAccount account = hospital.getAccount();
                    if (Objects.nonNull(account)) {
                        hospitalDto.setBalance(account.getBalance());
                        hospitalDto.setBudget(account.getBudget());
                    }
                    hospitalDtoList.add(hospitalDto);
                }
                ;
                return hospitalDtoList;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    @Transactional
    public Map<String, Object> registerHospital(AddHospitalDto addHospitalDto) throws MemberAssistanceException {
        LOGGER.info("Register a partner hospital.");

        Map<String, Object> result = new HashMap<>();
        Hospital hospital = new Hospital();
        try {
            final String hospitalName = addHospitalDto.getHospitalName();
            if (Objects.isNull(hospitalName)) {
                throw new MemberAssistanceException("Hospital name is null.");
            } // add other checks

            // TODO: Add checker for duplicate hospital name here
            // Hospital existingMember =
            // hospitalRepository.findByEmailAddress(emailAddress);
            List<Hospital> hospitals = hospitalRepository.findAll();
            for (Hospital hosp : hospitals) {
                if (hospitalName.equalsIgnoreCase(hosp.getHospitalName())) {
                    throw new MemberAssistanceException("Hospital already exists.");
                }
            }
            hospital.setHospitalName(hospitalName);
            hospital.setAddress1(addHospitalDto.getAddress1());
            hospital.setAddress2(addHospitalDto.getAddress2());

            String[] location = addHospitalDto.getLocation().split(",");
            String city = location[0];
            String region = location[1];
            if (Objects.isNull(city)) {
                throw new MemberAssistanceException("City is null. Please follow correct format for location.");
            }
            hospital.setCity(city.trim());

            if (Objects.isNull(region)) {
                throw new MemberAssistanceException("Region is null. Please follow correct format for location.");
            }
            hospital.setRegion(region.trim());

            // TODO: in the meantime set as created_by as NN
            //Date now = new Date();
            //hospital.setCreatedDate(now);
            // hospital.setCreatedBy(createdBy);

            Hospital saveHospital = hospitalRepository.save(hospital);
            if (Objects.nonNull(saveHospital)) {
                // result.put("isVerified", Boolean.FALSE);

                // Add hospital accounts
                HospitalAccount account = new HospitalAccount();
                BigDecimal budget = addHospitalDto.getAllotedBudget();
                account.setBudget(budget);
                account.setBalance(budget);
                //account.setCreatedDate(now);
                account.setHospital(saveHospital);
                saveHospital.setAccount(account);

                // Add hospital contact info
                HospitalContact contact = new HospitalContact();
                contact.setName(addHospitalDto.getContactPerson());
                contact.setContactNumber(addHospitalDto.getContactNumber());
                contact.setPosition(addHospitalDto.getDesignation());
                contact.setEmail(addHospitalDto.getEmailAddress());
                contact.setPrimary(true);
                //contact.setCreatedDate(now);
                contact.setHospital(saveHospital);

                Set<HospitalContact> contacts = new HashSet<HospitalContact>();
                contacts.add(contact);
                saveHospital.setContactSet(contacts);
                hospitalRepository.save(hospital);

                result.put("isRegistered", Boolean.TRUE);
                return result;
            }
        } catch (Exception e) {
            LOGGER.error("System error caused by: {}", e.getMessage(), e);
        }
        return null;
    }

    public Set<String> getAvailableHospitals() {
        // TODO: get hospitals with available budget;
        LOGGER.info("Get available hospitals");
        Set<String> hospitalNameLst = new HashSet<>();
        try {
            List<Hospital> hospitals = hospitalRepository.findAll();
            if (CollectionUtils.isNotEmpty(hospitals)) {
                hospitalNameLst = hospitals.stream().map(Hospital::getHospitalName).collect(Collectors.toSet());
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return hospitalNameLst;
    }

    @Override
    public Hospital isHospitalBalanceSufficient(String hospitalName, BigDecimal amount)
            throws MemberAssistanceException {
        LOGGER.info("Validate hospital medical assistance");

        Hospital hospital = null;
        try {
            hospital = hospitalRepository.findByHospitalName(hospitalName);

            if (Objects.isNull(hospital)) {
                throw new MemberAssistanceException("Hospital is missing.");
            }

            LOGGER.debug("Hospital Budget: {}", hospital.getAccount().getBalance());
            LOGGER.debug("hospital.getAccount().getBalance().compareTo(amount): {}",
                    hospital.getAccount().getBalance().compareTo(amount));

            if (hospital.getAccount().getBalance().compareTo(amount) < 0) {
                throw new MemberAssistanceException("Hospital has no enough budget.");
            }

            return hospital;
        } catch (MemberAssistanceException mae) {
            throw mae;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public HospitalDetailsDto getSelectedHospitalDetails(String hospitalName) throws MemberAssistanceException {
        LOGGER.info("Get selected hospital details");

        Hospital hospital = new Hospital();
        try {
            hospital = hospitalRepository.findByHospitalName(
                    hospitalName
            );

            if (Objects.isNull(hospital)) {
                throw new MemberAssistanceException("Hospital is missing.");
            }

            HospitalDetailsDto details = new HospitalDetailsDto();
            details.setHospitalId(hospital.getId());
            details.setHospitalName(hospital.getHospitalName());
            details.setLocation(hospital.getAddress1()
                .concat(", ").concat(hospital.getAddress2())
                .concat(", ").concat(hospital.getCity())
                .concat(", ").concat(hospital.getRegion()));
            
            Set<HospitalContact> contacts = hospital.getContactSet();
            Iterator<HospitalContact> it = contacts.iterator();
            while(it.hasNext()){
                HospitalContact contact = it.next();
                details.setContactPerson(contact.getName());
                details.setDesignation(contact.getPosition());
                details.setContactNumber(contact.getContactNumber());
                details.setEmailAddress(contact.getEmail());
            }            

            HospitalAccount account = hospital.getAccount();
            if (Objects.nonNull(account)) {
                details.setBalance(account.getBalance());
                details.setBudget(account.getBudget());
            }            

            return details;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public HospitalDetailsDto getMedicalAssistanceByHospital(String hospitalName, Integer pageIndex, Integer pageSize) throws MemberAssistanceException {
        LOGGER.info("Get the selected hospital's medical assistance details");
        try {
            Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("dateAwarded").descending());
            /* return medicalAssistanceRepository.findByHospitalName(
                    hospitalName,
                    pageable
            ); */
            List<MedicalAssistance> medicalAssistance = medicalAssistanceRepository.findByHospitalName(
                    hospitalName,
                    pageable
            );

            HospitalDetailsDto hospitalDetailsDto = new HospitalDetailsDto();
            List<MedicalAssistanceDetailsDto> details = new ArrayList<MedicalAssistanceDetailsDto>();
            int cnt = 0;
            for (MedicalAssistance assistance : medicalAssistance) {
                if (hospitalName.equalsIgnoreCase(assistance.getHospitalName())) {
                    MedicalAssistanceDetailsDto detail = new MedicalAssistanceDetailsDto();
                    detail.setFirstName(assistance.getFirstName());
                    detail.setMiddleName(Objects.isNull(assistance.getMiddleName())?"":assistance.getMiddleName());
                    detail.setLastName(assistance.getLastName());
                    detail.setVoucherNumber(assistance.getVoucherNumber());
                    detail.setDateAwarded(assistance.getDateAwarded());
                    detail.setAmount(assistance.getAmount());
                    detail.setMemberId(assistance.getMemberAccount().getAccountNumber());
                    details.add(detail);
                    if (assistance.getStatus().equalsIgnoreCase(MedicalAssistanceStatus.APPROVED.getValue())) {
                        cnt++;
                    }
                }
            }

            hospitalDetailsDto.setHospitalName(hospitalName);
            hospitalDetailsDto.setTotalAssistedCount(cnt);
            hospitalDetailsDto.setMedicalAssistanceList(details);

            return hospitalDetailsDto;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Unable to retrieve medical assistance by hospital");
        }
    }

    @Transactional
    @Override
    public Boolean saveAdditionalBudget(MemberAccount account,
                                        HospitalDetailsDto dto) throws MemberAssistanceException {
        LOGGER.info("Save additional budget for: {}.", dto.getHospitalName());
        try {

            Hospital hospital = hospitalRepository.findByHospitalName(dto.getHospitalName());
            if (Objects.isNull(hospital)) {
                throw new MemberAssistanceException("Hospital is missing.");
            }

            Date today = new Date();
            String username = account.getEmailAddress();
            
            // Add hospital transaction info
            HospitalTransaction transaction = new HospitalTransaction();
            BigDecimal additionalBudget = dto.getAmount();
            BigDecimal oldBudget = hospital.getAccount().getBudget();
            BigDecimal oldBalance = hospital.getAccount().getBalance();
            hospital.getAccount().setBalance(additionalBudget.add(oldBalance));
            hospital.getAccount().setBudget(additionalBudget.add(oldBudget));
            hospital.getAccount().setUpdatedBy(username);
            hospital.getAccount().setUpdatedDate(today);
            transaction.setHospital(hospital);
            transaction.setAmount(additionalBudget);
            transaction.setMemberId(account.getMember().getId());            
            transaction.setCreatedDate(today);
            transaction.setCreatedBy(username);

            HospitalTransaction saved = hospitalTransactionRepository.save(transaction);
            if (Objects.nonNull(saved)) {
                LOGGER.info("Save additional budget successful");
                return true;
            }
            return false;
        } catch (MemberAssistanceException mae) {
            throw mae;
        } catch (Exception e) {
            LOGGER.info(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public List<HospitalDetailsDto> getSelectedHospitalBudgetHistory(String hospitalName) throws MemberAssistanceException {
        LOGGER.info("Get budget replenish history of the selected hospital.");

        Hospital hospital = new Hospital();
        try {
            hospital = hospitalRepository.findByHospitalName(
                    hospitalName
            );

            if (Objects.isNull(hospital)) {
                throw new MemberAssistanceException("Hospital is missing.");
            }

            List<HospitalDetailsDto> dto = new ArrayList<HospitalDetailsDto>();
            Set<HospitalTransaction> transactions = hospital.getTransactionSet();
            Iterator<HospitalTransaction> it = transactions.iterator();
            while(it.hasNext()){
                HospitalTransaction transaction = it.next();
                HospitalDetailsDto details = new HospitalDetailsDto();
                details.setAmount(transaction.getAmount());
                details.setDateCreated(transaction.getCreatedDate());
                dto.add(details);
            }
            return dto;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public HospitalDto getAllHospitalsSummary(List<HospitalDto> hospitalList) {
        LOGGER.info("Get summary details of all hospitals.");
        try {
            
            if (Objects.nonNull(hospitalList)) {
                BigDecimal totalBalance = new BigDecimal(0);
                BigDecimal totalBudget = new BigDecimal(0);

                HospitalDto hospitalDto = new HospitalDto();
                for (HospitalDto hospital : hospitalList) {
                    totalBalance = hospital.getBalance().add(totalBalance);
                    totalBudget = hospital.getBudget().add(totalBudget);                    
                }
                hospitalDto.setTotalBalance(totalBalance);
                hospitalDto.setTotalBudget(totalBudget);

                //Check total assisted count                
                hospitalDto.setTotalAssistedCount(medicalAssistanceRepository.getTotalAssistedCount(MedicalAssistanceStatus.APPROVED.getValue()));
                
                return hospitalDto;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void deductVoucherAmount(MedicalAssistance latestMedicalAssistance) {
        LOGGER.info("Deduct voucher amount.");
        try {
            Long hospitalId = latestMedicalAssistance.getHospitalId();
            Hospital hospital = hospitalRepository.findById(hospitalId).get();
            HospitalAccount account = hospital.getAccount();
            account.setBalance(account.getBalance().subtract(latestMedicalAssistance.getAmount()));
            hospitalAccountRepository.save(account);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
