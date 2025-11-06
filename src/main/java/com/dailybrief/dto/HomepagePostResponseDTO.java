package com.dailybrief.dto;

import java.util.List;

public record HomepagePostResponseDTO(
    LocalizedPostResponseDTO latestPost,
    List<LocalizedPostResponseDTO> recentPosts
) {}