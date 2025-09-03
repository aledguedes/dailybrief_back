package com.dailybrief.dto;

public record AutomationResponseDTO(
    Long id,
    String theme,
    String outputFormat,
    String category,
    Boolean generateSocial,
    String image,
    String author,
    String readTime
) {}

