package com.sgta.invoice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceEnrichedResponseDto(
        Long id,
        Long workOrderId,
        String workOrderCode,
        Long customerId,
        String customerName,
        String number,
        String receiptType,
        BigDecimal subtotal,
        BigDecimal tax,
        BigDecimal total,
        LocalDateTime issueDate
) {
}
