package com.dailybrief.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;

public record PostRequestDTO(
                @NotEmpty(message = "Title must contain at least one translation") @Size(min = 3, max = 3, message = "Title must contain translations for PT, EN, and ES") Map<String, String> title,

                @NotEmpty(message = "Excerpt must contain at least one translation") @Size(min = 3, max = 3, message = "Excerpt must contain translations for PT, EN, and ES") Map<String, String> excerpt,

                @NotEmpty(message = "Content must contain at least one translation") @Size(min = 3, max = 3, message = "Content must contain translations for PT, EN, and ES") Map<String, String> content,

                List<String> imageIds,
                String author,
                List<String> tags,
                Integer categoryId,

                @NotEmpty(message = "Meta description must contain at least one translation") @Size(min = 3, max = 3, message = "Meta description must contain translations for PT, EN, and ES") Map<String, String> metaDescription,

                Map<String, String> affiliateLinks,
                Integer statusId,
                String publishedAt,
                String readTime) {
}
