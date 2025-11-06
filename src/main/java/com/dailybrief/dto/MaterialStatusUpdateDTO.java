package com.dailybrief.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public record MaterialStatusUpdateDTO(
    @NotNull(message = "Status ID cannot be null")
    @Min(value = 1, message = "Status ID must be a positive number")
    Integer statusId
) {
}