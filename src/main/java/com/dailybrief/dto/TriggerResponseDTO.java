package com.dailybrief.dto;

public record TriggerResponseDTO(
        String triggerId,
        String message,
        String taskId,
        String status) {
}