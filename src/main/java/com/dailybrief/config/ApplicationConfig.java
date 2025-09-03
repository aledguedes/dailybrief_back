package com.dailybrief.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {


    @Value("${python.api.url}")
    private String pythonApiUrl;
    
    public String getPythonApiUrl() {
        return pythonApiUrl;
    }
}
