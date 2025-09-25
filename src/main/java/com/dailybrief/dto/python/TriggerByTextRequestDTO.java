package com.dailybrief.dto.python;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TriggerByTextRequestDTO(
        @JsonProperty("text_content") String textContent,
        @JsonProperty("content_type") String contentType) {
}
