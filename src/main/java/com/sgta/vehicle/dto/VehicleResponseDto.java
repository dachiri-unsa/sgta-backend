package com.sgta.vehicle.dto;

public record VehicleResponseDto(
    Long id,
    Long customerId,
    String licensePlate,
    String brand,
    String model,
    Short year,
    String color,
    String vin,
    Integer mileage,
    String fuelType,
    String transmission,
    boolean status
) {}
