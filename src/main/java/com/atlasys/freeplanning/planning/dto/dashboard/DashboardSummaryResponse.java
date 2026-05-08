package com.atlasys.freeplanning.planning.dto.dashboard;

import java.math.BigDecimal;

public record DashboardSummaryResponse(
        Long activeProjectsCount,
        BigDecimal monthBilling,
        Long deliveriesThisWeek
) {
}
