package com.member.assistance.api.configuration;

import com.member.assistance.api.exception.CustomResponseEntityExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebMvcConfiguration {

    @Bean (name = "customResponseEntityExceptionHandler")
    CustomResponseEntityExceptionHandler customResponseEntityExceptionHandler() {
        CustomResponseEntityExceptionHandler customResponseEntityExceptionHandler = new CustomResponseEntityExceptionHandler();
        return customResponseEntityExceptionHandler;
    }
}
