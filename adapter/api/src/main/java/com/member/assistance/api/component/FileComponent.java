package com.member.assistance.api.component;

import com.member.assistance.core.service.FileService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class FileComponent {
    private static final Logger LOG
            = LogManager.getLogger(FileComponent.class);

    @Autowired
    FileService fileService;

    @PostConstruct
    public void init() {
        LOG.info("Init file configurations.");
        fileService.init();
    }
}
