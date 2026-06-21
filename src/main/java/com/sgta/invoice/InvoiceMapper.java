package com.sgta.invoice;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.sgta.invoice.dto.CreateInvoiceDto;
import com.sgta.invoice.dto.InvoiceEnrichedResponseDto;
import com.sgta.invoice.dto.InvoiceResponseDto;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface InvoiceMapper {

    @Mapping(target = "workOrderId", source = "workOrder.id")
    @Mapping(target = "customerId", source = "customer.id")
    InvoiceResponseDto toResponse(Invoice invoice);

    @Mapping(target = "workOrderId", source = "workOrder.id")
    @Mapping(target = "workOrderCode", source = "workOrder.code")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", expression = "java(invoice.getCustomer().getFirstName() + \" \" + invoice.getCustomer().getLastName())")
    InvoiceEnrichedResponseDto toEnrichedResponse(Invoice invoice);

    @Mapping(target = "status", constant = "true")
    @Mapping(target = "workOrder", ignore = true)
    @Mapping(target = "customer", ignore = true)
    Invoice toEntity(CreateInvoiceDto dto);

    @Mapping(target = "workOrder", ignore = true)
    @Mapping(target = "customer", ignore = true)
    void update(CreateInvoiceDto dto, @MappingTarget Invoice invoice);

}
