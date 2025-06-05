package com.compdes.auth.users.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

/**
 * DTO utilizado para registrar un nuevo usuario en el sistema Compdes.
 * Contiene la información básica requerida: nombre de usuario, contraseña y el
 * rol a asignar.
 * 
 * Las validaciones aseguran que los campos no estén vacíos y cumplan con los
 * límites establecidos.
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

    @NotBlank(message = "Selecciona un rol para el usuario.")
    String roleLabel;

}
