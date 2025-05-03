package com.dailybrief.dto;

import java.time.Instant;

public record LogRequestDTO(
        String action, // Ex.: "Post #1 aprovado"
        String user, // Ex.: "admin"
        Instant timestamp) {
}