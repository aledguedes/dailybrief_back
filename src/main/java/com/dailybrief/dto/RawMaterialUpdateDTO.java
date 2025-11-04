package com.dailybrief.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RawMaterialUpdateDTO(
    @NotBlank(message = "Content cannot be empty")
    @Size(min = 10, message = "Content must have at least 10 characters")
    String content
) {
}