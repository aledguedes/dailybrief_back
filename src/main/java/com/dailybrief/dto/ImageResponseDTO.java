package com.dailybrief.dto;

import java.util.UUID;

public record ImageResponseDTO(
        UUID id,
        String url,
        String publicId) {
}
