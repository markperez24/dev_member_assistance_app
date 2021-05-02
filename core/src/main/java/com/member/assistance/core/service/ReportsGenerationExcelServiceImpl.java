package com.member.assistance.core.service;

import com.member.assistance.common.utility.DateUtils;
import com.member.assistance.common.utility.MemberAssistanceUtils;
import com.member.assistance.core.constants.MemberAssistanceConstants;
import com.member.assistance.core.constants.ReportFileType;
import com.member.assistance.core.dto.HospitalReportDto;
import com.member.assistance.core.dto.MemberReportDto;
import com.member.assistance.core.exception.MemberAssistanceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Service(ReportFileType.ReportTypeContants.EXCEL)
public class ReportsGenerationExcelServiceImpl implements ReportsGenerationService {
    private static final Logger LOGGER = LogManager.getLogger(ReportsGenerationExcelServiceImpl.class);
    private SXSSFWorkbook workbook;
    private SXSSFSheet sheet;

    @Autowired
    FileServiceImpl fileService;

    @Override
    public ReportsGenerationService generate(String reportFileType) {
        return null;
    }

    @Override
    public void generateMemberReports(List<MemberReportDto> memberList, boolean eof) throws MemberAssistanceException {
        LOGGER.info("Generate member reports.");
        try {
            LinkedList<Map<String, Object>> memberLinkedList = new LinkedList<>();
            for (MemberReportDto memberReportDto : memberList) {
                Class<?> clazz = Class.forName("com.member.assistance.core.dto.MemberReportDto");
                Map<String, Object> memberMap = new LinkedHashMap();
                for (Map.Entry<String, String> entry : MemberAssistanceConstants.MEMBER_REPORTS_COLUMN_HEADERS
                        .entrySet()) {
                    String key = entry.getKey();
                    Field field = clazz.getDeclaredField(key);
                    field.setAccessible(true);
                    Object value = (Object) field.get(memberReportDto);
                    memberMap.put(key, value);
                }
                memberLinkedList.add(memberMap);
            }

            // Generate the excel
            workbook = new SXSSFWorkbook();
            sheet = workbook.createSheet(MemberAssistanceConstants.MEMBER_REPORTS_SHEET);
            sheet.trackColumnsForAutoSizing(Arrays.asList());

            // header
            writeHeaderLine(MemberAssistanceConstants.MEMBER_REPORTS_COLUMN_HEADERS);
            // data
            writeDataLines(MemberAssistanceConstants.MEMBER_REPORTS_COLUMN_HEADERS, memberLinkedList);

            Path reports = FileServiceImpl.getReports();
            String nowDateStr = DateUtils.convertDateToDateStr(new Date(), DateUtils.DATE_FORMAT_DATE);
            String filename = new StringJoiner("/")
                    .add(reports.toAbsolutePath().toString()).add(MemberAssistanceConstants.MEMBER_REPORT_FILENAME
                            .concat("-").concat(nowDateStr).concat(".").concat(ReportFileType.EXCEL.getValue()))
                    .toString();
            try (FileOutputStream out = new FileOutputStream(filename)) {
                workbook.write(out);
            }
            ;
            workbook.dispose();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Failed to generate member reports.");
        }
    }

    @Override
    public void generateHospitalReports(List<HospitalReportDto> hospitalList, boolean eof, String itemName)
            throws MemberAssistanceException {
        LOGGER.info("Generate hospital reports.");
        try {
            //Dynamic Hospital Report's Headers (Budget | Balance)
            LinkedHashMap<String, String> dynamicHeaders = new LinkedHashMap<String, String>();
            if (MemberAssistanceConstants.LBL_REPORTS_ITEM_NAME_BALANCE.equalsIgnoreCase(itemName)) {
                dynamicHeaders = MemberAssistanceConstants.HOSPITAL_REPORTS_COLUMN_DYNAMIC_HEADERS_BALANCE;
            } else if (MemberAssistanceConstants.LBL_REPORTS_ITEM_NAME_BUDGET.equalsIgnoreCase(itemName)) {
                dynamicHeaders = MemberAssistanceConstants.HOSPITAL_REPORTS_COLUMN_DYNAMIC_HEADERS_BUDGET;
            }

            LinkedList<Map<String, Object>> hospitalLinkedList = new LinkedList<>();
            for (HospitalReportDto hospitalReportDto : hospitalList) {
                Class<?> clazz = Class.forName("com.member.assistance.core.dto.HospitalReportDto");
                Map<String, Object> hospitalMap = new LinkedHashMap();
                for (Map.Entry<String, String> entry : dynamicHeaders.entrySet()) {
                    String key = entry.getKey();
                    Field field = clazz.getDeclaredField(key);
                    field.setAccessible(true);
                    Object value = (Object) field.get(hospitalReportDto);
                    hospitalMap.put(key, value);
                }
                hospitalLinkedList.add(hospitalMap);
            }

            // Generate the excel
            workbook = new SXSSFWorkbook();
            sheet = workbook.createSheet(MemberAssistanceConstants.HOSPITAL_REPORTS_SHEET);
            sheet.trackColumnsForAutoSizing(Arrays.asList());

            // header
            writeHeaderLine(dynamicHeaders);
            // data
            writeDataLines(dynamicHeaders, hospitalLinkedList);

            Path reports = FileServiceImpl.getReports();
            String nowDateStr = DateUtils.convertDateToDateStr(new Date(), DateUtils.DATE_FORMAT_DATE);
            String filename = new StringJoiner("/")
                    .add(reports.toAbsolutePath().toString()).add(MemberAssistanceConstants.HOSPITAL_REPORT_FILENAME
                            .concat("-").concat(nowDateStr).concat(".").concat(ReportFileType.EXCEL.getValue()))
                    .toString();
            try (FileOutputStream out = new FileOutputStream(filename)) {
                workbook.write(out);
            }
            ;
            workbook.dispose();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Failed to generate hospital reports.");
        }
    }

    @Override
    public ReportsGenerationService export(String reportFileType) {
        return null;
    }

    @Override
    public Map<String, Object> exportMemberReports() throws MemberAssistanceException {
        LOGGER.info("Export excel member reports.");
        try {
            Map<String, Object> map = new HashMap<>();
            Path reports = FileServiceImpl.getReports();
            String nowDateStr = DateUtils.convertDateToDateStr(new Date(), DateUtils.DATE_FORMAT_DATE);
            String filename = new StringJoiner("/")
                    .add(reports.toAbsolutePath().toString()).add(MemberAssistanceConstants.MEMBER_REPORT_FILENAME
                            .concat("-").concat(nowDateStr).concat(".").concat(ReportFileType.EXCEL.getValue()))
                    .toString();
            File file = new File(filename);
            // Here you have to set the actual filename of your pdf
            InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
            map.put("filename", filename);
            map.put("file", isr);
            return map;
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Member reports not generated.");
        } catch (Exception e) {
            LOGGER.error("Failed to export member reports.");
            throw  new MemberAssistanceException("Failed to export member reports.");
        }
    }

    @Override
    public Map<String, Object> exportHospitalReports() throws MemberAssistanceException {
        LOGGER.info("Export excel hospital reports.");
        try {
            Map<String, Object> map = new HashMap<>();
            Path reports = FileServiceImpl.getReports();
            String nowDateStr = DateUtils.convertDateToDateStr(new Date(), DateUtils.DATE_FORMAT_DATE);
            String filename = new StringJoiner("/")
                    .add(reports.toAbsolutePath().toString()).add(MemberAssistanceConstants.HOSPITAL_REPORT_FILENAME
                            .concat("-").concat(nowDateStr).concat(".").concat(ReportFileType.EXCEL.getValue()))
                    .toString();
            File file = new File(filename);
            // Here you have to set the actual filename of your pdf
            InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
            map.put("filename", filename);
            map.put("file", isr);
            return map;
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Hospital report not yet generated.");
        } catch (Exception e) {
            LOGGER.error("Failed to export hospital reports.");
            throw  new MemberAssistanceException("Failed to export hospital report.");
        }
    }

    private void writeHeaderLine(LinkedHashMap<String, String> headerLinkedHashMap) throws MemberAssistanceException {
        LOGGER.info("Create header line.");
        try {
            Row row = sheet.createRow(0);

            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);

            int columnCount = 0;
            for (Map.Entry<String, String> entry : headerLinkedHashMap.entrySet()) {
                createCell(row, columnCount++, entry.getValue(), style);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Failed to generate reports, creating header details.");
        }
    }

    private void writeDataLines(LinkedHashMap<String, String> headers, LinkedList<Map<String, Object>> dataLinkList)
            throws MemberAssistanceException {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        style.setFont(font);

        LOGGER.info("Write data row.");
        try {
            for (Map<String, Object> linkedHashMap : dataLinkList) {
                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;
                for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
                    createCell(row, columnCount++, linkedHashMap.get(headerEntry.getKey()), style);
                }
            }
        } catch (MemberAssistanceException mae) {
            throw mae;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MemberAssistanceException("Failed to generate reports, creating data rows.");
        }

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) throws MemberAssistanceException {
        try {
            LOGGER.info("Cell Row: {}; Column:{}; Value: {}", row.getRowNum(), columnCount,
                    MemberAssistanceUtils.convertObjectToJsonString(value));
            sheet.trackColumnForAutoSizing(columnCount);
            sheet.autoSizeColumn(columnCount);
            Cell cell = row.createCell(columnCount);
            LOGGER.info("Value: {}", value);
            if (value instanceof Integer) {
                cell.setCellValue((Integer) value);
            } else if (value instanceof BigDecimal) {
                cell.setCellValue(String.valueOf(value));
            } else if (value instanceof Boolean) {
                cell.setCellValue((Boolean) value);
            } else if(value instanceof Date){
                //TODO: add date or datetime logic
                cell.setCellValue(DateUtils.convertDateToDateStr((Date) value, DateUtils.DATE_FORMAT_DATE));
            } else {
                cell.setCellValue((String) value);
            }
            cell.setCellStyle(style);
        } catch (ClassCastException cce) {
            throw new MemberAssistanceException("Failed to generate reports, data type not recognize.");
        }
    }
}
