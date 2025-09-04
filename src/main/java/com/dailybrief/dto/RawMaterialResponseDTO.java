package com.dailybrief.dto;

import java.util.UUID;

public record RawMaterialResponseDTO(
        UUID materialId,
        String url,
        UUID taskId,
        UUID triggerId,
        String status,
        String createdAt) {
}