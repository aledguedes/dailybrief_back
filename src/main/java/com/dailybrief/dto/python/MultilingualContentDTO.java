package com.dailybrief.dto.python;

import jakarta.validation.constraints.NotNull;

public record MultilingualContentDTO(
        @NotNull String title,
        @NotNull String excerpt,
        @NotNull String content,
        @NotNull String metaDescription) {
}