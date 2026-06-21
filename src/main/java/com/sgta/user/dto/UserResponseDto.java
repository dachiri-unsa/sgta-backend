package com.sgta.user.dto;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponseDto(
    Long id,
    String name,
    String email,
    LocalDateTime lastLogin,
    String specialty,
    String phone,
    List<String> roles
) {}
