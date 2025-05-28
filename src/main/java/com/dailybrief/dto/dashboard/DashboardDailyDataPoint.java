package com.dailybrief.dto.dashboard;

import java.time.LocalDate;

public record DashboardDailyDataPoint(
                LocalDate date,
                Long value) {
}