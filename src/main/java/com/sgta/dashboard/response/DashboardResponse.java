package com.sgta.dashboard.response;

import java.util.Collections;
import java.util.List;

public record DashboardResponse(
        long customerCount,
        long vehicleCount,
        long partCount,
        long workOrderCount,
        long invoiceCount,
        long userCount,
        StockAnalytics stockAnalytics,
        WorkOrderAnalytics workOrderAnalytics,
        List<RecentWorkOrderDto> recentWorkOrders
) {
    public DashboardResponse {
        if (stockAnalytics == null) stockAnalytics = StockAnalytics.ZERO;
        if (workOrderAnalytics == null) workOrderAnalytics = WorkOrderAnalytics.ZERO;
        if (recentWorkOrders == null) recentWorkOrders = Collections.emptyList();
    }
}
