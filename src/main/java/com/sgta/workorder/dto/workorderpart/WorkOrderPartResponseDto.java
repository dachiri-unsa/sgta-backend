package com.sgta.workorder.dto.workorderpart;

import java.math.BigDecimal;

public record WorkOrderPartResponseDto(
        Long id,
        Long partId,
        Long workOrderId,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}
