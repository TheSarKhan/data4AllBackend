package org.example.dataprotal.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
        Long totalUsers,
        Long thisMonthUsers,
        BigDecimal totalEarnings,
        List<LogResponse> lastActivities
) {
}
