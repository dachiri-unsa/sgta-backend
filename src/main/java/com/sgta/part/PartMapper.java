package com.sgta.part;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.sgta.part.dto.CreatePartDto;
import com.sgta.part.dto.PartResponseDto;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PartMapper {

    PartResponseDto toResponse(Part part);

    @Mapping(target = "status", constant = "true")
    Part toEntity(CreatePartDto dto);

    void update(CreatePartDto dto, @MappingTarget Part part);

}
