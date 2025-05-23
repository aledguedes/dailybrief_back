package com.dailybrief.dto;

import java.time.Instant;

public record LogResponseDTO(
        Long id,
        String action,
        String created_by,
        Instant timestamp) {
}
