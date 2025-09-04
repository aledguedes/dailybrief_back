package com.dailybrief.dto;

import java.util.Optional;

public record GeneratedContentDTO(
        Optional<String> title,
        Optional<String> content,
        Optional<String> imageUrl,
        Optional<String> imageUrlBase64) {
}