package com.sgta.vehicle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CreateVehicleDto(

    @NotNull(message = "Customer ID is required")
    Long customerId,

    @NotBlank(message = "License plate is required")
    @Size(max = 20, message = "License plate must be at most 20 characters")
    String licensePlate,

    @NotBlank(message = "Brand is required")
    @Size(max = 100, message = "Brand must be at most 100 characters")
    String brand,

    @NotBlank(message = "Model is required")
    @Size(max = 100, message = "Model must be at most 100 characters")
    String model,

    Short year,

    @Size(max = 50, message = "Color must be at most 50 characters")
    String color,

    @Size(max = 100, message = "VIN must be at most 100 characters")
    String vin,

    @NotNull(message = "Mileage is required")
    @PositiveOrZero(message = "Mileage must be zero or positive")
    Integer mileage,

    @Size(max = 50, message = "Fuel type must be at most 50 characters")
    String fuelType,

    @Size(max = 50, message = "Transmission must be at most 50 characters")
    String transmission

) {}
