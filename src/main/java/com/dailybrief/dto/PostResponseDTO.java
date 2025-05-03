package com.dailybrief.dto;

import java.util.List;
import java.util.Map;

public record PostResponseDTO(
		Long id,
		Map<String, String> title,
		Map<String, String> excerpt,
		Map<String, String> content,
		String image,
		String author,
		List<String> tags,
		String category,
		Map<String, String> metaDescription,
		Map<String, String> affiliateLinks,
		String status,
		String date,
		String readTime) {
}