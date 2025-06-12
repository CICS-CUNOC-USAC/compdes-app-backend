package com.compdes.auth.users.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * DTO base utilizado para la creación de usuarios en el sistema COMPDES.
 * 
 * Contiene los campos esenciales requeridos para registrar un usuario:
 * nombre de usuario y contraseña. Esta clase puede ser extendida por otros
 * DTOs que requieran datos adicionales según el tipo de usuario a registrar.
 * 
 * Incluye validaciones para garantizar que los campos no estén vacíos y que
 * cumplan con los límites de longitud definidos.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-01
 */
@Getter
public class CreateCompdesUserDTO {

    @NotBlank(message = "Por favor, ingresa un nombre de usuario.")
    @Size(max = 50, message = "El nombre de usuario debe tener como máximo 50 caracteres.")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    String password;

    public CreateCompdesUserDTO(
            @NotBlank(message = "Por favor, ingresa un nombre de usuario.") @Size(max = 50, message = "El nombre de usuario debe tener como máximo 50 caracteres.") String username,
            @NotBlank(message = "Por favor, ingresa una contraseña.") @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres.") String password) {
        this.username = username;
        this.password = password;
    }

}
