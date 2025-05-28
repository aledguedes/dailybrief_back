package com.dailybrief.dto;

import java.time.Instant;
import java.util.Map;

public record LogResponseDTO(
                Long id,
                String reportId,
                String level,
                String action,
                String created_by,
                Map<String, Object> details,
                Instant timestamp) {
}