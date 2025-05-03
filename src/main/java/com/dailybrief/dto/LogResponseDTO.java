package com.dailybrief.dto;

import java.time.Instant;

public record LogResponseDTO(
        Long id,
        String action, // Ex.: "Post #1 aprovado"
        String user, // Ex.: "admin"
        Instant timestamp) {
}
