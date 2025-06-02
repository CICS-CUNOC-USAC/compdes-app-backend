package com.compdes.auth.users.models.dto.request;

import com.compdes.auth.users.enums.RolesEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-01
 */
@Value
public class CreateCompdesUserDTO {
    
    @NotBlank(message = "Por favor, ingresa un nombre de usuario.")
    @Size(max = 50, message = "El nombre de usuario debe tener como máximo 50 caracteres.")
    String username;

    @NotBlank(message = "Por favor, ingresa una contraseña.")
    @Size(max = 100, message = "La contraseña no debe superar los 100 caracteres.")
    String password;

    @NotNull(message = "Selecciona un rol para el usuario.")
    RolesEnum role;

}
