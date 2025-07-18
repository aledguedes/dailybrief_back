package com.dailybrief.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TrendingTopicSuggestionDTO(
        @NotBlank @Size(max = 255) String topicName,
        @NotBlank @Size(max = 50) String source,
        @NotBlank @Size(max = 1000) String relevanceReason,
        @Size(max = 500) String url,
        @NotBlank @Size(max = 20) String status) {
}