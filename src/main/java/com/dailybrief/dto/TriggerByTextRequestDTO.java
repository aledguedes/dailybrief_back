package com.dailybrief.dto;

import jakarta.validation.constraints.NotBlank;

public record TriggerByTextRequestDTO(@NotBlank String text) {
}