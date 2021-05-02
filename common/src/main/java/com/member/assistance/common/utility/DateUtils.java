package com.member.assistance.common.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    private static final Logger LOGGER = LogManager.getLogger(DateUtils.class);
    public static final String DATE_FORMAT_DATE = "yyyy-MM-dd";
    public static final String DATE_FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    public static String convertDateToDateStr(Date date, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        } catch (Exception e) {
            LOGGER.error("Failed to format date, caused by: {}", e.getMessage(), e);
            return null;
        }
    }
}
