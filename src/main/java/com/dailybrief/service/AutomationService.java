package com.dailybrief.service;

import com.dailybrief.dto.AutomationDTO;
import com.dailybrief.dto.TrendingTopicSuggestionDTO;

import java.util.List;

public interface AutomationService {
    String saveAutomationRequest(AutomationDTO dto, String jwtToken);

    void saveSuggestions(List<TrendingTopicSuggestionDTO> dtos);

    List<TrendingTopicSuggestionDTO> getSuggestionsByStatus(String status);

    TrendingTopicSuggestionDTO updateSuggestion(Long id, TrendingTopicSuggestionDTO dto);
}