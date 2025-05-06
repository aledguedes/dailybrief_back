package com.dailybrief.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;

public record PostRequestDTO(
    @NotEmpty(message = "Title must contain at least one translation")
    @Size(min = 3, max = 3, message = "Title must contain translations for PT, EN, and ES")
    Map<String, String> title, // PT, EN, ES

    @NotEmpty(message = "Excerpt must contain at least one translation")
    @Size(min = 3, max = 3, message = "Excerpt must contain translations for PT, EN, and ES")
    Map<String, String> excerpt, // PT, EN, ES

    @NotEmpty(message = "Content must contain at least one translation")
    @Size(min = 3, max = 3, message = "Content must contain translations for PT, EN, and ES")
    Map<String, String> content, // PT, EN, ES

    String image,
    String author,
    List<String> tags,
    String category,

    @NotEmpty(message = "Meta description must contain at least one translation")
    @Size(min = 3, max = 3, message = "Meta description must contain translations for PT, EN, and ES")
    Map<String, String> metaDescription, // SEO

    Map<String, String> affiliateLinks, // Hotmart, ClickBank, Amazon
    String status, // PENDING, APPROVED, REJECTED
    String publishedAt, // ISO 8601 format
    String readTime // Ex.: "5 min"
) {}