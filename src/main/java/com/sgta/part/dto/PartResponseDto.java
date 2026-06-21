package com.sgta.part.dto;

import java.math.BigDecimal;

public record PartResponseDto(
    Long id,
    String code,
    String name,
    String description,
    String category,
    String brand,
    Integer stock,
    Integer minimumStock,
    String location,
    BigDecimal purchasePrice,
    BigDecimal salePrice,
    boolean status
) {}
