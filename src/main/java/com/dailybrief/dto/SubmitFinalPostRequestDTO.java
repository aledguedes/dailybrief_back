package com.dailybrief.dto;

import java.util.List;
import java.util.Map;

public record SubmitFinalPostRequestDTO(
    Map<String, String> title,
    Map<String, String> excerpt,
    Map<String, String> content,
    Map<String, String> metaDescription,
    String image,
    String author,
    List<String> tags,
    String category,
    Map<String, String> affiliateLinks,
    String status,
    String publishedAt,
    String readTime,
    String taskIdToDelete
) {}
