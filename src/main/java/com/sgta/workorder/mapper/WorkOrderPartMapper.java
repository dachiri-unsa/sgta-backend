package com.sgta.workorder.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.sgta.workorder.dto.workorderpart.CreateWorkOrderPartDto;
import com.sgta.workorder.dto.workorderpart.WorkOrderPartResponseDto;
import com.sgta.workorder.model.WorkOrderPart;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface WorkOrderPartMapper {

    @Mapping(target = "partId", source = "part.id")
    @Mapping(target = "workOrderId", source = "workOrder.id")
    WorkOrderPartResponseDto toResponse(WorkOrderPart workOrderPart);

    @Mapping(target = "status", constant = "true")
    @Mapping(target = "part", ignore = true)
    @Mapping(target = "workOrder", ignore = true)
    WorkOrderPart toEntity(CreateWorkOrderPartDto dto);

    @Mapping(target = "part", ignore = true)
    @Mapping(target = "workOrder", ignore = true)
    void update(CreateWorkOrderPartDto dto, @MappingTarget WorkOrderPart workOrderPart);

}
