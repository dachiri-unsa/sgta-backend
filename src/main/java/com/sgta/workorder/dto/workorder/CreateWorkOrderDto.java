package com.sgta.workorder.dto.workorder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CreateWorkOrderDto(
        @NotBlank(message = "El código de la orden de trabajo es obligatorio")
        @Size(max = 50, message = "El código no debe superar los 50 caracteres")
        String code,

        @NotNull(message = "El identificador del cliente es obligatorio")
        @Positive(message = "El identificador del cliente debe ser un número positivo")
        Long customerId,

        @NotNull(message = "El identificador del vehículo es obligatorio")
        @Positive(message = "El identificador del vehículo debe ser un número positivo")
        Long vehicleId,

        @Positive(message = "El identificador del mecánico debe ser un número positivo")
        Long mechanicUserId,

        @Size(max = 50, message = "El estado del vehículo no debe superar los 50 caracteres")
        String vehicleStatus,

        @Size(max = 20, message = "La prioridad no debe superar los 20 caracteres")
        String priority,

        @NotNull(message = "La fecha de ingreso es obligatoria")
        LocalDateTime intakeDate,

        LocalDateTime estimatedDate,

        LocalDateTime deliveryDate,

        @NotBlank(message = "El problema reportado es obligatorio")
        @Size(max = 10000, message = "El problema reportado no debe superar los 10000 caracteres")
        String reportedProblem,

        @Size(max = 10000, message = "El diagnóstico no debe superar los 10000 caracteres")
        String diagnosis,

        @Size(max = 10000, message = "Las observaciones no deben superar los 10000 caracteres")
        String observations,

        @PositiveOrZero(message = "El kilometraje de ingreso debe ser cero o un número positivo")
        Integer intakeMileage,

        @Size(max = 50, message = "El nivel de combustible no debe superar los 50 caracteres")
        String fuelLevel,

        @Size(max = 10000, message = "Los accesorios no deben superar los 10000 caracteres")
        String accessories,

        @Size(max = 10000, message = "Los daños visibles no deben superar los 10000 caracteres")
        String visibleDamage,

        @NotNull(message = "El total de repuestos es obligatorio")
        @DecimalMin(value = "0.00", message = "El total de repuestos no puede ser negativo")
        @DecimalMax(value = "9999999999.99", message = "El total de repuestos excede el monto máximo permitido")
        @Digits(integer = 10, fraction = 2, message = "El total de repuestos debe tener como máximo 10 dígitos enteros y 2 decimales")
        BigDecimal totalParts,

        @NotNull(message = "El total de mano de obra es obligatorio")
        @DecimalMin(value = "0.00", message = "El total de mano de obra no puede ser negativo")
        @DecimalMax(value = "9999999999.99", message = "El total de mano de obra excede el monto máximo permitido")
        @Digits(integer = 10, fraction = 2, message = "El total de mano de obra debe tener como máximo 10 dígitos enteros y 2 decimales")
        BigDecimal totalLabor,

        @NotNull(message = "El subtotal es obligatorio")
        @DecimalMin(value = "0.00", message = "El subtotal no puede ser negativo")
        @DecimalMax(value = "9999999999.99", message = "El subtotal excede el monto máximo permitido")
        @Digits(integer = 10, fraction = 2, message = "El subtotal debe tener como máximo 10 dígitos enteros y 2 decimales")
        BigDecimal subtotal,

        @NotNull(message = "El IGV es obligatorio")
        @DecimalMin(value = "0.00", message = "El IGV no puede ser negativo")
        @DecimalMax(value = "9999999999.99", message = "El IGV excede el monto máximo permitido")
        @Digits(integer = 10, fraction = 2, message = "El IGV debe tener como máximo 10 dígitos enteros y 2 decimales")
        BigDecimal igv,

        @NotNull(message = "El total es obligatorio")
        @DecimalMin(value = "0.00", message = "El total no puede ser negativo")
        @DecimalMax(value = "9999999999.99", message = "El total excede el monto máximo permitido")
        @Digits(integer = 10, fraction = 2, message = "El total debe tener como máximo 10 dígitos enteros y 2 decimales")
        BigDecimal total,

        @Size(max = 30, message = "El estado no debe superar los 30 caracteres")
        String workOrderStatus
) {
}
