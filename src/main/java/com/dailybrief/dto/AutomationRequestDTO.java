package com.dailybrief.dto;

import jakarta.validation.constraints.NotBlank;

public record AutomationRequestDTO(@NotBlank String url) {
}