package com.dailybrief.dto;

import jakarta.validation.constraints.NotNull;

public record AutomationDTO(@NotNull(message = "output_format é obrigatório") String outputFormat,

		String theme) {
}