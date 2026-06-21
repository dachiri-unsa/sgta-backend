package com.sgta.workorder.dto.workorder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WorkOrderEnrichedResponseDto(
        Long id,
        String code,
        Long customerId,
        String customerName,
        Long vehicleId,
        String vehicleInfo,
        Long mechanicUserId,
        String mechanicName,
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
