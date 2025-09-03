package com.dailybrief.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map; 

public record MaterialProcessResponseDTO(
    Long id,
    String userId,
    String taskId,
    String theme,
    String rawMaterial,
    List<String> sourceUrls, 
    String contentType,
    String status,
    Map<String, Object> generatedContent, 
    String suggestedImagePrompt, 
    String errorMessage,
    Instant createdAt,
    Instant updatedAt
) {}