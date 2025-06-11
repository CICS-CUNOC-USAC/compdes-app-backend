package com.compdes.auth.users.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * DTO utilizado para registrar usuarios del sistema que no son participantes.
 * 
 * Esta clase extiende {@link CreateCompdesUserDTO} e incluye el campo
 * adicional {@code roleLabel}, que permite especificar el rol que se asignará
 * al nuevo usuario (exceptuando el rol {@code PARTICIPANT}, que está
 * reservado).
 * 
 * Es empleado en operaciones de registro de usuarios administrativos o técnicos
 * dentro del sistema COMPDES.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-10
 */
@Getter
public class CreateNonParticipantCompdesUserDTO extends CreateCompdesUserDTO {

    @NotBlank(message = "Selecciona un rol para el usuario.")
    private String roleLabel;

    public CreateNonParticipantCompdesUserDTO(
            @NotBlank(message = "Por favor, ingresa un nombre de usuario.") @Size(max = 50, message = "El nombre de usuario debe tener como máximo 50 caracteres.") String username,
            @NotBlank(message = "Por favor, ingresa una contraseña.") @Size(max = 100, message = "La contraseña no debe superar los 100 caracteres.") String password,
            @NotBlank(message = "Selecciona un rol para el usuario.") String roleLabel) {
        super(username, password);
        this.roleLabel = roleLabel;
    }

}
