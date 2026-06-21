package com.sgta.dashboard.response;

public record StockAnalytics(
        long adequate,
        long low,
        long outOfStock
) {
    public static final StockAnalytics ZERO = new StockAnalytics(0, 0, 0);
}
