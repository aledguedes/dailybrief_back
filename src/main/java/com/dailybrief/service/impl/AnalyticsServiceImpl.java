package com.dailybrief.service.impl;

import com.dailybrief.dto.AnalyticsDTO;
import com.dailybrief.service.AnalyticsService;

import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Override
    public AnalyticsDTO getAnalytics() {
        // Simular métricas (substituir por integração real)
        return new AnalyticsDTO(1000, 50);
    }
}
