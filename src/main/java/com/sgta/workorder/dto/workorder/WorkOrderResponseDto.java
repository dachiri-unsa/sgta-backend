package com.sgta.workorder.dto.workorder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WorkOrderResponseDto(
        Long id,
        String code,
        Long customerId,
        Long vehicleId,
        Long mechanicUserId,
        String vehicleStatus,
        String priority,
        LocalDateTime intakeDate,
        LocalDateTime estimatedDate,
        LocalDateTime deliveryDate,
        String reportedProblem,
        String diagnosis,
        String observations,
        Integer intakeMileage,
        String fuelLevel,
        String accessories,
        String visibleDamage,
        BigDecimal totalParts,
        BigDecimal totalLabor,
        BigDecimal subtotal,
        BigDecimal igv,
        BigDecimal total,
        String workOrderStatus
) {
}
