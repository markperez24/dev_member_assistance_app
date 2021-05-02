package com.member.assistance.core.service;

import com.member.assistance.backend.model.Hospital;
import com.member.assistance.backend.model.Member;
import com.member.assistance.backend.repository.HospitalRepository;
import com.member.assistance.backend.repository.MemberRepository;
import com.member.assistance.core.constants.MemberAssistanceConstants;
import com.member.assistance.core.dto.HospitalReportDto;
import com.member.assistance.core.dto.HospitalReportRequestDto;
import com.member.assistance.core.dto.MemberReportDto;
import com.member.assistance.core.dto.MemberReportRequestDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ReportServiceImpl implements ReportService {
    private static Logger LOGGER = LogManager.getLogger(ReportServiceImpl.class);

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    HospitalRepository hospitalRepository;

    @Autowired
    ReportsGenerationServiceImpl reportsGenerationService;

    @Override
    public Map<String, Object> getMembers(String sName,
                                          String sLocation,
                                          String sGender,
                                          String sDateOfBirth,
                                          Integer pageIndex,
                                          Integer pageSize) throws MemberAssistanceException {
        LOGGER.info("Get members, search by, name: {}, location:{}, " +
                "gender: {}, date of birth: {}", sName, sLocation, sGender, sDateOfBirth);
        try {
            Map<String, Object> memberMap = new HashMap<>();

            final Sort.Order sortByCreatedDate = new Sort.Order(
                    Sort.Direction.DESC,
                    "createdDate"
            );

            final Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(
                sortByCreatedDate
            ));

            Page<Member> memberPage = memberRepository.getMemberByNameAndLocationAndGenderAndDateOfBirth(
                    sName,
                    sLocation,
                    sGender,
                    sDateOfBirth,
                    pageable
            );

            List<MemberReportDto> memberReportDtoList = new ArrayList<>();
            if(memberPage.getSize() > 0) {
                for (Member member : memberPage.getContent()) {
                    MemberReportDto memberReportDto = new MemberReportDto(member);
                    memberReportDtoList.add(memberReportDto);
                }
            }
            memberMap.put("members", memberReportDtoList);
            memberMap.put("total", memberPage.getTotalElements());
            return memberMap;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Failed to get members.");
        }
    }

    @Override
    public void generateMemberReports(MemberReportRequestDto memberReportRequestDto)
            throws MemberAssistanceException {
        final String sName = memberReportRequestDto.getsName();
        final String sGender = memberReportRequestDto.getsGender();
        final String sLocation = memberReportRequestDto.getsLocation();
        final String sDateOfBirth = memberReportRequestDto.getsDateOfBirth();

        LOGGER.info("Generate member reports, search by, name: {}, location:{}, " +
                "gender: {}, date of birth: {}", sName, sLocation, sGender, sDateOfBirth);
        try {
            final String reportType = memberReportRequestDto.getType();
            LOGGER.info("With report type: {}", reportType);

            final Sort.Order sortByCreatedDate = new Sort.Order(
                    Sort.Direction.DESC,
                    "createdDate"
            );
            Pageable pageable = PageRequest.of(0, 1000, Sort.by(sortByCreatedDate));
            Page<Member> pageResult;
            while (true) {
                pageResult = memberRepository.getMemberByNameAndLocationAndGenderAndDateOfBirth(
                        sName, sLocation, sGender, sDateOfBirth, pageable
                );
                List<MemberReportDto> reportDtoList = new ArrayList<>();
                if(pageResult.getSize() > 0) {
                    for (Member member : pageResult.getContent()) {
                        MemberReportDto memberReportDto = new MemberReportDto(member);
                        reportDtoList.add(memberReportDto);
                    }
                }
                reportsGenerationService.generate(reportType)
                        .generateMemberReports(reportDtoList, !pageResult.hasNext());
                if (!pageResult.hasNext()) {
                    break;
                }
                pageable = pageResult.nextPageable();
            }

            //if(ReportType.EXCEL.getValue().equalsIgnoreCase(reportType))

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Failed to generate member reports.");
        }
    }

    @Override
    public Map<String, Object> getHospitals(String sName,
                                          String sLocation,
                                          String sItem,
                                          Date sDateFrom,
                                          Date sDateTo,
                                          Integer pageIndex,
                                          Integer pageSize) throws MemberAssistanceException {
        LOGGER.info("Get hospitals, search by, name: {}, location:{}, " +
                "item: {}, date from: {}, date to: {}", sName, sLocation, sItem, sDateFrom, sDateTo);
        try {
            Map<String, Object> hospitalMap = new HashMap<>();

            final Sort.Order sortByCreatedDate = new Sort.Order(
                    Sort.Direction.DESC,
                    "createdDate"
            );

            final Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(
                sortByCreatedDate
            ));

            Page<Hospital> hospitalPage = hospitalRepository.getHospitalByNameAndLocationAndItemAndDate(
                    sName,
                    sLocation,
                    sDateFrom,
                    sDateTo,
                    pageable
            );

            //Default/Initial Item
            if (Objects.isNull(sItem)) {
                sItem = MemberAssistanceConstants.LBL_REPORTS_ITEM_NAME_BUDGET;
            }

            List<HospitalReportDto> hospitalReportDtoList = new ArrayList<>();
            if(hospitalPage.getSize() > 0) {
                for (Hospital hospital : hospitalPage.getContent()) {
                    HospitalReportDto hospitalReportDto = new HospitalReportDto(hospital, sItem);
                    hospitalReportDtoList.add(hospitalReportDto);
                }
            }

            hospitalMap.put("hospitals", hospitalReportDtoList);
            hospitalMap.put("total", hospitalPage.getTotalElements());
            return hospitalMap;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Failed to get hospitals.");
        }
    }

    @Override
    public void generateHospitalReports(HospitalReportRequestDto hospitalReportRequestDto)
            throws MemberAssistanceException {
        final String sName = hospitalReportRequestDto.getsName();
        final String sLocation = hospitalReportRequestDto.getsLocation();
        final Date sDateFrom = hospitalReportRequestDto.getsDateFrom();
        final Date sDateTo = hospitalReportRequestDto.getsDateTo();
        String sItem = hospitalReportRequestDto.getsItem();

        LOGGER.info("Generate hospital reports, search by, name: {}, location:{}, " +
                "item: {}, date from: {}, date to: {}", sName, sLocation, sItem, sDateFrom, sDateTo);
        try {
            final String reportType = hospitalReportRequestDto.getType();
            LOGGER.info("With report type: {}", reportType);

            final Sort.Order sortByCreatedDate = new Sort.Order(
                    Sort.Direction.DESC,
                    "createdDate"
            );
            Pageable pageable = PageRequest.of(0, 1000, Sort.by(sortByCreatedDate));
            Page<Hospital> pageResult;
            
            while (true) {
                pageResult = hospitalRepository.getHospitalByNameAndLocationAndItemAndDate(
                        sName, sLocation, sDateFrom, sDateTo, pageable
                );

                //Default/Initial Item
                if (Objects.isNull(sItem)) {
                    sItem = MemberAssistanceConstants.LBL_REPORTS_ITEM_NAME_BUDGET;
                }
                
                List<HospitalReportDto> reportDtoList = new ArrayList<>();
                if(pageResult.getSize() > 0) {
                    for (Hospital hospital : pageResult.getContent()) {
                        HospitalReportDto hospitalReportDto = new HospitalReportDto(hospital, sItem);
                        reportDtoList.add(hospitalReportDto);
                    }
                }
                reportsGenerationService.generate(reportType)
                        .generateHospitalReports(reportDtoList, !pageResult.hasNext(), sItem);
                if (!pageResult.hasNext()) {
                    break;
                }
                pageable = pageResult.nextPageable();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Failed to generate hospital reports.");
        }
    }

    @Override
    public Map<String, Object> downloadMemberReport(String reportType) throws MemberAssistanceException {
        LOGGER.info("Download member reports.");
        try {
            return reportsGenerationService.export(reportType)
                    .exportMemberReports();
        } catch (MemberAssistanceException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Failed to download member report.");
        }
    }

    @Override
    public Map<String, Object> downloadHospitalReport(String reportType) throws MemberAssistanceException {
        LOGGER.info("Download member reports.");
        try {
            return reportsGenerationService.export(reportType)
                    .exportHospitalReports();
        } catch (MemberAssistanceException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Failed to download hospital report.");
        }
    }
}
