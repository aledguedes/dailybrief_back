package com.dailybrief.dto;

import java.time.Instant;

public record LogRequestDTO(
        String action, // Ex.: "Post #1 aprovado"
        String created_by, // Ex.: "admin"
        Instant timestamp) {
}