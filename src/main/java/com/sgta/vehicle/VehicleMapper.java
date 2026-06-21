package com.sgta.vehicle;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.sgta.vehicle.dto.CreateVehicleDto;
import com.sgta.vehicle.dto.VehicleEnrichedResponseDto;
import com.sgta.vehicle.dto.VehicleResponseDto;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface VehicleMapper {

    @Mapping(target = "customerId", source = "customer.id")
    VehicleResponseDto toResponse(Vehicle vehicle);

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", expression = "java(vehicle.getCustomer().getFirstName() + \" \" + vehicle.getCustomer().getLastName())")
    VehicleEnrichedResponseDto toEnrichedResponse(Vehicle vehicle);

    @Mapping(target = "status", constant = "true")
    @Mapping(target = "customer", ignore = true)
    Vehicle toEntity(CreateVehicleDto dto);

    @Mapping(target = "customer", ignore = true)
    void update(CreateVehicleDto dto, @MappingTarget Vehicle vehicle);

}
