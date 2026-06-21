package com.sgta.vehicle.dto;

public record VehicleMinimalDto(
        Long id,
        Long customerId,
        String licensePlate,
        String brand,
        String model
) {
}
