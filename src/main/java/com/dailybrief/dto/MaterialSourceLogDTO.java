package com.dailybrief.dto;

import java.time.ZonedDateTime;

public record MaterialSourceLogDTO(
        String id,
        String url,
        String status,
        String rawId,
        ZonedDateTime createdAt) {
}