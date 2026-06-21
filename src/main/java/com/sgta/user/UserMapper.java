package com.sgta.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.sgta.user.dto.CreateUserDto;
import com.sgta.user.dto.UpdateUserDto;
import com.sgta.user.dto.UserResponseDto;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(user.getRoleUsers().stream().filter(com.sgta.role.model.RoleUser::isStatus).map(ru -> ru.getRole().getName()).toList())")
    UserResponseDto toResponse(User user);

    @Mapping(target = "status", constant = "true")
    User toEntity(CreateUserDto dto);

    void update(UpdateUserDto dto, @MappingTarget User user);

}
