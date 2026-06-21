package com.sgta.workorder.dto.workorderpart;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateWorkOrderPartDto(
        @NotNull(message = "El identificador del repuesto es obligatorio")
        @Positive(message = "El identificador del repuesto debe ser un número positivo")
        Long partId,

        @NotNull(message = "El identificador de la orden de trabajo es obligatorio")
        @Positive(message = "El identificador de la orden de trabajo debe ser un número positivo")
        Long workOrderId,

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser un número positivo")
        Integer quantity,

        @NotNull(message = "El precio unitario es obligatorio")
        @DecimalMin(value = "0.00", message = "El precio unitario no puede ser negativo")
        @DecimalMax(value = "9999999999.99", message = "El precio unitario excede el monto máximo permitido")
        @Digits(integer = 10, fraction = 2, message = "El precio unitario debe tener como máximo 10 dígitos enteros y 2 decimales")
        BigDecimal unitPrice,

        @NotNull(message = "El subtotal es obligatorio")
        @DecimalMin(value = "0.00", message = "El subtotal no puede ser negativo")
        @DecimalMax(value = "9999999999.99", message = "El subtotal excede el monto máximo permitido")
        @Digits(integer = 10, fraction = 2, message = "El subtotal debe tener como máximo 10 dígitos enteros y 2 decimales")
        BigDecimal subtotal
) {
}
