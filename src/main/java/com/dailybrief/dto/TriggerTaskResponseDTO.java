package com.dailybrief.dto;

import java.util.UUID;

public record TriggerTaskResponseDTO(
        UUID id,
        UUID triggerId,
        String taskId,
        String message,
        String status) {
}