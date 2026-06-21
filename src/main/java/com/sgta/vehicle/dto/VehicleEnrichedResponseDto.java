package com.sgta.vehicle.dto;

public record VehicleEnrichedResponseDto(
        Long id,
        Long customerId,
        String customerName,
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
) {
}
