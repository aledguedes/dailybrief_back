package com.dailybrief.dto.python;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TriggerByUrlRequestDTO(
        @JsonProperty("url") String url,
        @JsonProperty("theme") String theme,
        @JsonProperty("content_type") String contentType) {
}
