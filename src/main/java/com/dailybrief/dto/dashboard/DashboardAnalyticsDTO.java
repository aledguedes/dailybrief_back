package com.dailybrief.dto.dashboard;

import java.util.List;

public record DashboardAnalyticsDTO(MetricDataDTO totalPageviews, MetricDataDTO totalAffiliateClicks,
		MetricDataDTO totalConversions, Long totalPostsPublished, Long activeUsers,

		Long totalPostsApproved, Long totalPostsPending, Long totalPostsRejected,

		List<DashboardDailyDataPoint> dailyPageviews, List<DashboardDailyDataPoint> dailyAffiliateClicks,
		List<DashboardTrafficSource> trafficSources) {
}
