package com.dailybrief.dto;

public record MaterialProcessInitialDTO(
        String userId,
        String taskId,
        String theme,
        String contentType,
        String status) {
}
