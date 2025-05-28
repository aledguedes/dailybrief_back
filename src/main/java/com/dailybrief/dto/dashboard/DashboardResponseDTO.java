package com.dailybrief.dto.dashboard;

import com.dailybrief.dto.LogResponseDTO;
import java.util.List;

public record DashboardResponseDTO(
        DashboardAnalyticsDTO analytics,
        List<DashboardPostResponseDTO> recentPosts,
        List<LogResponseDTO> recentLogs) {
}