package com.sgta.dashboard.response;

public record WorkOrderAnalytics(
        long completed,
        long pending
) {
    public static final WorkOrderAnalytics ZERO = new WorkOrderAnalytics(0, 0);
}
