package com.dailybrief.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequestDTO(
		@NotBlank(message = "Category name is required") @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters") String name,

		@Size(max = 255, message = "Description must not exceed 255 characters") String description,

		@NotBlank(message = "Target audience is required") String targetAudience) {
}