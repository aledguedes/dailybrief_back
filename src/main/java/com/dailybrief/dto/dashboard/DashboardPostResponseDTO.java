package com.dailybrief.dto.dashboard;

import java.util.Map;

public record DashboardPostResponseDTO(
                Long id,
                String featuredImage,
                Map<String, String> title,
                Map<String, String> excerpt,
                String status,
                String category) {
}