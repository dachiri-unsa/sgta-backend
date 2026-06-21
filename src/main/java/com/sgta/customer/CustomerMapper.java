package com.sgta.customer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.sgta.customer.dto.CreateCustomerDto;
import com.sgta.customer.dto.CustomerResponseDto;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CustomerMapper {

    CustomerResponseDto toResponse(Customer customer);

    @Mapping(target = "status", constant = "true")
    Customer toEntity(CreateCustomerDto dto);

    void update(CreateCustomerDto dto, @MappingTarget Customer customer);

}
