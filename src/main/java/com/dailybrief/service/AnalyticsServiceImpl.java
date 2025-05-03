package com.dailybrief.service;

import com.dailybrief.dto.AnalyticsDTO;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Override
    public AnalyticsDTO getAnalytics() {
        // Simular métricas (substituir por integração real)
        return new AnalyticsDTO(1000, 50);
    }
}
