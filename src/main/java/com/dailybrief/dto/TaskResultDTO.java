package com.dailybrief.dto;

import java.util.Optional;

public record TaskResultDTO(
        String taskId,
        String status,
        Optional<String> message,
        Optional<GeneratedContentDTO> result) {
}