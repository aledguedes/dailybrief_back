package com.dailybrief.dto.python;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TriggerByTextRequestDTO(String textContent, @JsonProperty("content_type") String contentType,
		String provider) {
}