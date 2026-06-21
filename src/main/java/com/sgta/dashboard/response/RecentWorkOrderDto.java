package com.sgta.dashboard.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RecentWorkOrderDto(
        Long id,
        String code,
        String priority,
        LocalDateTime intakeDate,
        BigDecimal total
) {
}
