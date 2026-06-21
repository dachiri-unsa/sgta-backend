package com.sgta.workorder.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.sgta.workorder.dto.workorder.CreateWorkOrderDto;
import com.sgta.workorder.dto.workorder.WorkOrderEnrichedResponseDto;
import com.sgta.workorder.dto.workorder.WorkOrderResponseDto;
import com.sgta.workorder.model.WorkOrder;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface WorkOrderMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "vehicleId", source = "vehicle.id")
    @Mapping(target = "mechanicUserId", source = "mechanicUser.id")
    WorkOrderResponseDto toResponse(WorkOrder workOrder);

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", expression = "java(workOrder.getCustomer().getFirstName() + \" \" + workOrder.getCustomer().getLastName())")
    @Mapping(target = "vehicleId", source = "vehicle.id")
    @Mapping(target = "vehicleInfo", expression = "java(workOrder.getVehicle().getLicensePlate() + \" \\u2014 \" + workOrder.getVehicle().getBrand() + \" \" + workOrder.getVehicle().getModel())")
    @Mapping(target = "mechanicUserId", source = "mechanicUser.id")
    @Mapping(target = "mechanicName", expression = "java(workOrder.getMechanicUser() != null ? workOrder.getMechanicUser().getName() : null)")
    WorkOrderEnrichedResponseDto toEnrichedResponse(WorkOrder workOrder);

    @Mapping(target = "status", constant = "true")
    @Mapping(target = "workOrderStatus", defaultValue = "PENDIENTE")
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "mechanicUser", ignore = true)
    WorkOrder toEntity(CreateWorkOrderDto dto);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "mechanicUser", ignore = true)
    void update(CreateWorkOrderDto dto, @MappingTarget WorkOrder workOrder);

}
