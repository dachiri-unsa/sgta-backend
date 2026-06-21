package com.sgta.customer.dto;

import jakarta.validation.constraints.*;

public record CreateCustomerDto(

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
    String firstName,

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
    String lastName,

    @NotBlank(message = "Document number is required")
    @Size(min = 8, max = 20, message = "Document number must be between 8 and 20 characters")
    String documentNumber,

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "\\d{9,20}", message = "Phone must be numeric")
    @Size(min = 9, max = 20, message = "Phone must be between 9 and 20 digits")
    String phone,

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(min = 5, max = 150, message = "Email must be between 5 and 150 characters")
    String email,

    @NotBlank(message = "Address is required")
    String address

) {}
