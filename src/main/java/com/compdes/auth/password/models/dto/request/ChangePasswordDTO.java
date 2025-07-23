package com.compdes.auth.password.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

/**
 * DTO utilizado para representar la solicitud de cambio de contraseña.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-07-22
 */
@Value
public class ChangePasswordDTO {

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, max = 100, message = "La contraseña debe tener al menos 8 caracteres")
    String newPassword;
}
