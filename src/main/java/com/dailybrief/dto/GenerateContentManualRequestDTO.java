package com.dailybrief.dto;

import java.util.List;

public record GenerateContentManualRequestDTO(
        String userId,
        String taskId,
        String theme,
        String rawMaterial,
        String contentType,
        List<String> keywords,
        String tone,
        String ctaInstruction,
        List<String> articleStructure,
        String audience,
        String idealArticleExample) {
}