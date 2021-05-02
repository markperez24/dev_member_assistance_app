package com.member.assistance.core.service;

import com.member.assistance.backend.model.ConfigParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ConfigParamService {
    ConfigParam getConfigByCodeAndType(String code, String type);
    List<ConfigParam> getConfigByCode(String code);
    List<ConfigParam> getConfigByType(String type);
    List<ConfigParam> getConfigInTypeList(List<String> asList);
    String getConfigValue(String code, String type);
}
