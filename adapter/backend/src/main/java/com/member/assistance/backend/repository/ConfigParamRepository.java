package com.member.assistance.backend.repository;

import com.member.assistance.backend.model.ConfigParam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConfigParamRepository extends JpaRepository<ConfigParam, Long> {
    List<ConfigParam> findByCode(String code);
    List<ConfigParam> findByType(String type);
    ConfigParam findByCodeAndType(String code, String type);
    List<ConfigParam> findByTypeIn(List<String> configTypes);
}
