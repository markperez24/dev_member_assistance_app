package com.member.assistance.common.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

public class MemberAssistanceUtils {
    private static final Logger LOGGER = LogManager.getLogger(MemberAssistanceUtils.class);
    private static final String UTF_8 = "UTF-8";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper() {{
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
        configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }};

    public static Map<String, Object> convertObjectToMap(Object obj) {
        return OBJECT_MAPPER.convertValue(obj, HashMap.class);
    }

    public static Map<String, Object> convertObjectToMap(Object obj, ObjectMapper objMapper) {
        if(Objects.nonNull(objMapper)){
            return objMapper.convertValue(obj, HashMap.class);
        }
        return OBJECT_MAPPER.convertValue(obj, HashMap.class);
    }

    public static List<Object> convertObjectToList(Object obj) {
        return OBJECT_MAPPER.convertValue(obj, ArrayList.class);
    }

    public static Map<String, Object> convertJsonStringToMap(String json) {
        Map<String, Object> map = null;
        String properJson = json;
        final TypeReference<HashMap<String, Object>> typeRefHashMap = new TypeReference<HashMap<String, Object>>() {
            /* Intended Empty */
        };
        try {
            if(StringUtils.isBlank(json)){
                return null;
            }
            LOGGER.info(">>> convertJsonStringToMap(): default.");
            map = OBJECT_MAPPER.readValue(properJson, typeRefHashMap);
        } catch (final Exception e) {
            LOGGER.warn(e.getMessage());
            try {
                // If original entry not parse-able, un-escape JSON string.
                properJson = StringUtils.trim(StringUtils.unwrap(json, '"'));
                properJson = StringEscapeUtils.unescapeJson(properJson);
                LOGGER.info(">>> convertJsonStringToMap(): unescape.");
                map = OBJECT_MAPPER.readValue(properJson, typeRefHashMap);
            } catch (final Exception e2) {
                LOGGER.error(e.getMessage(), e2);
            }
        }
        return map;
    }

    public static List<Object> convertJsonStringToList(String json) {
        List<Object> ls = null;
        final TypeReference<List<Object>> typeRefList = new TypeReference<List<Object>>() {
            /* Intended Empty */
        };
        try {
            if(StringUtils.isBlank(json)){
                return null;
            }
            LOGGER.info(">>> convertJsonStringToList(String)");
            ls = OBJECT_MAPPER.readValue(json, typeRefList);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage());
        }
        return ls;
    }

    public static String convertObjectToJsonString(Object obj) {
        ObjectWriter ow = OBJECT_MAPPER.writer().withDefaultPrettyPrinter();
        String json = null;
        try {
            json = ow.writeValueAsString(obj);
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return json;
    }

    public static Byte[] toByteArray(byte[] bytes) {
        return IntStream.range(0, bytes.length)
                .mapToObj(i -> bytes[i])
                .toArray(Byte[]::new);
    }
}

