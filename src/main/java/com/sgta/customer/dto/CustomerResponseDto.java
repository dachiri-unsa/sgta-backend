package com.sgta.customer.dto;

public record CustomerResponseDto(
    Long id,
    String firstName,
    String lastName,
    String documentNumber,
    String phone,
    String email,
    String address
) {
    
}
