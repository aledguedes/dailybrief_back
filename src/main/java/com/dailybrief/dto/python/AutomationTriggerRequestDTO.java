package com.dailybrief.dto.python;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AutomationTriggerRequestDTO(
        @JsonProperty("urls") List<String> urlsList,
        @JsonProperty("theme") String theme,
        @JsonProperty("outputFormat") String outputFormat,
        @JsonProperty("userId") String userId) {
}
