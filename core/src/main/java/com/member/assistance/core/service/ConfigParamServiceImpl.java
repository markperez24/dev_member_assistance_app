package com.member.assistance.core.service;

import com.member.assistance.backend.model.ConfigParam;
import com.member.assistance.backend.repository.ConfigParamRepository;
import com.member.assistance.common.utility.MemberAssistanceUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class ConfigParamServiceImpl implements ConfigParamService {
    private static final Logger LOGGER = LogManager.getLogger(ConfigParamServiceImpl.class);

    @Autowired
    private ConfigParamRepository configParamRepository;

    private final static BiFunction<List<ConfigParam>, String, String> fGetConfigValue
            = (configList, code) -> configList.stream().filter(c -> c.getCode().equalsIgnoreCase(code))
            .findFirst().map(ConfigParam::getValue)
            .orElseGet(null);

    @Override
    public List<ConfigParam> getConfigByCode(String code) {
        LOGGER.debug("Get config by code: {}.", code);
        try {
            return configParamRepository.findByCode(code);
        } catch (Exception e) {
            LOGGER.error("Failed to get config by code.");
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public ConfigParam getConfigByCodeAndType(String code, String type) {
        LOGGER.debug("Get config by code: {} and type: {}.", code, type);
        try {
            return configParamRepository.findByCodeAndType(code, type);
        } catch (Exception e) {
            LOGGER.error("Failed to get config by code and type.");
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<ConfigParam> getConfigByType(String type) {
        LOGGER.debug("Get config by type: {}.", type);
        try {
            return configParamRepository.findByType(type);
        } catch (Exception e) {
            LOGGER.error("Failed to get config by type.");
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<ConfigParam> getConfigInTypeList(List<String> configTypes) {
        LOGGER.debug("Get config by types: {}.", MemberAssistanceUtils.convertObjectToJsonString(configTypes));
        try {
            return configParamRepository.findByTypeIn(configTypes);
        } catch (Exception e) {
            LOGGER.error("Failed to get config by type.");
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String getConfigValue(String code, String type) {
        LOGGER.debug("Get config value w/ code:{} and type: {}.", code, type);
        String val = null;
        try {
            ConfigParam config = configParamRepository.findByCodeAndType(code, type);
            if (Objects.nonNull(config)) {
                val = config.getValue();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to get config value.");
            LOGGER.error(e.getMessage(), e);
        }
        return val;
    }


}
