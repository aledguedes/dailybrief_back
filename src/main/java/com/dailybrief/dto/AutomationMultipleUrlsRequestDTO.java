package com.dailybrief.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record AutomationMultipleUrlsRequestDTO(@NotEmpty List<String> urls,
        @NotEmpty String theme,
        @NotEmpty String outputFormat) {
}
