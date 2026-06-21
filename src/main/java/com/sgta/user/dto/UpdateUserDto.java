package com.sgta.user.dto;

import java.util.List;

import com.sgta.role.model.RoleName;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserDto(

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre debe tener máximo 100 caracteres")
    String name,

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    @Size(max = 150, message = "El email debe tener máximo 150 caracteres")
    String email,

    @Size(min = 6, max = 255, message = "La contraseña debe tener entre 6 y 255 caracteres")
    String password,

    @Size(max = 100, message = "La especialidad debe tener máximo 100 caracteres")
    String specialty,

    @Size(max = 20, message = "El teléfono debe tener máximo 20 caracteres")
    String phone,

    @NotEmpty(message = "Debe seleccionar al menos un rol")
    List<@NotNull RoleName> roles

) {}
