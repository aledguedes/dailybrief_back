package com.dailybrief.dto;

import java.util.List;

public record LocalizedPostResponseDTO(
        Long id,
        String title,
        String excerpt,
        String content,
        String image,
        String author,
        List<String> tags,
        String category,
        String metaDescription,
        String affiliateLinks,
        String status,
        String date,
        String readTime
) {}
