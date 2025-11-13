package com.dailybrief.dto;

import java.util.List;
import java.util.Map;

public record PostResponseDTO(
		String id,
		Map<String, String> title,
		Map<String, String> excerpt,
		Map<String, String> content,
		List<ImageResponseDTO> images,
		String author,
		List<String> tags,
		CategoryResponseDTO category,
		Map<String, String> metaDescription,
		Map<String, String> affiliateLinks,
		StatusDTO status,
		String publishedAt,
		String readTime,
		String createdAt,
		String updatedAt) {
}
