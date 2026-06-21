package com.sgta.invoice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceResponseDto(
    Long id,
    Long workOrderId,
    Long customerId,
    String number,
    String receiptType,
    BigDecimal subtotal,
    BigDecimal tax,
    BigDecimal total,
    LocalDateTime issueDate
) {}
