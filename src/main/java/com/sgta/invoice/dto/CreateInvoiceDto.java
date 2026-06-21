package com.sgta.invoice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateInvoiceDto(

    @NotNull(message = "El ID de la orden de trabajo es obligatorio")
    @PositiveOrZero(message = "El ID de la orden de trabajo debe ser positivo o cero")
    Long workOrderId,

    @NotNull(message = "El ID del cliente es obligatorio")
    @PositiveOrZero(message = "El ID del cliente debe ser positivo o cero")
    Long customerId,

    @NotBlank(message = "El número de factura es obligatorio")
    @Size(max = 50, message = "El número de factura debe tener como máximo 50 caracteres")
    String number,

    @NotBlank(message = "El tipo de comprobante es obligatorio")
    @Size(max = 50, message = "El tipo de comprobante debe tener como máximo 50 caracteres")
    String receiptType,

    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.00", inclusive = true, message = "El subtotal no puede ser negativo")
    @Digits(integer = 10, fraction = 2, message = "El subtotal debe tener como máximo 10 dígitos enteros y 2 decimales")
    BigDecimal subtotal,

    @NotNull(message = "El impuesto es obligatorio")
    @DecimalMin(value = "0.00", inclusive = true, message = "El impuesto no puede ser negativo")
    @Digits(integer = 10, fraction = 2, message = "El impuesto debe tener como máximo 10 dígitos enteros y 2 decimales")
    BigDecimal tax,

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.00", inclusive = true, message = "El total no puede ser negativo")
    @Digits(integer = 10, fraction = 2, message = "El total debe tener como máximo 10 dígitos enteros y 2 decimales")
    BigDecimal total,

    @NotNull(message = "La fecha de emisión es obligatoria")
    LocalDateTime issueDate

) {}
