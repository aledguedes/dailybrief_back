package com.dailybrief.service;

import com.dailybrief.dto.AutomationDTO;
import com.dailybrief.dto.AutomationResponseDTO;
import com.dailybrief.dto.TrendingTopicSuggestionDTO;

import java.util.List;
import java.util.Optional;

public interface AutomationService {
    String saveAutomationRequest(AutomationDTO dto, String jwtToken);

    void saveSuggestions(List<TrendingTopicSuggestionDTO> dtos);

    List<TrendingTopicSuggestionDTO> getSuggestionsByStatus(String status);

    TrendingTopicSuggestionDTO updateSuggestion(Long id, TrendingTopicSuggestionDTO dto);

    Optional<AutomationResponseDTO> findById(Long id);
}