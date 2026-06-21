package com.sgta.part.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreatePartDto(

    @NotBlank(message = "Code is required")
    @Size(max = 50, message = "Code must be at most 50 characters")
    String code,

    @NotBlank(message = "Name is required")
    @Size(max = 150, message = "Name must be at most 150 characters")
    String name,

    @Size(max = 500, message = "Description must be at most 500 characters")
    String description,

    @Size(max = 100, message = "Category must be at most 100 characters")
    String category,

    @Size(max = 100, message = "Brand must be at most 100 characters")
    String brand,

    @NotNull(message = "Stock is required")
    @PositiveOrZero(message = "Stock must be zero or positive")
    Integer stock,

    @NotNull(message = "Minimum stock is required")
    @PositiveOrZero(message = "Minimum stock must be zero or positive")
    Integer minimumStock,

    @Size(max = 100, message = "Location must be at most 100 characters")
    String location,

    @NotNull(message = "Purchase price is required")
    @DecimalMin(value = "0.01", message = "Purchase price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Purchase price must have at most 10 integer digits and 2 decimal places")
    BigDecimal purchasePrice,

    @NotNull(message = "Sale price is required")
    @DecimalMin(value = "0.01", message = "Sale price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Sale price must have at most 10 integer digits and 2 decimal places")
    BigDecimal salePrice

) {}
