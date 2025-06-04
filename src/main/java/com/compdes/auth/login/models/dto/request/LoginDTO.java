package com.compdes.auth.login.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

/**
 * DTO para representar los datos de inicio de sesión de un usuario.
 * Contiene el correo electrónico y la contraseña requeridos para autenticarse.
 *
 * Esta clase es inmutable y utiliza Lombok para generar automáticamente
 * constructor, getters, equals, hashCode y toString.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-03
 */
@Value
public class LoginDTO {

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    String password;
}
