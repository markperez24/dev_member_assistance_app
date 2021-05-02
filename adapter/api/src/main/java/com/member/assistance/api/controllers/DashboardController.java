package com.member.assistance.api.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/dashboard")
public class DashboardController {

    private static Logger LOGGER = LogManager.getLogger(DashboardController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/")
    @ResponseBody
    public Map<String, Object> loadHome() {
        LOGGER.info("Loading home.");

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("success", "OK");

        return resultMap;
    }
}
