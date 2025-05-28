package com.dailybrief.service;

import com.dailybrief.dto.AutomationDTO;

public interface AutomationService {
    String saveAutomationRequest(AutomationDTO dto, String jwtToken);
}