package com.compdes.auth.users.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * DTO utilizado para completar el registro de un usuario del sistema asociado a
 * un participante.
 * 
 * Extiende {@link CreateCompdesUserDTO} e incorpora el campo obligatorio
 * {@code identificationDocument}, correspondiente al documento de
 * identificación del participante.
 * 
 * Este DTO se utiliza cuando un usuario previamente inicializado sin
 * credenciales completa su registro
 * proporcionando nombre de usuario, contraseña y su documento de identidad.
 * 
 * Las validaciones aseguran que los datos ingresados cumplan con los requisitos
 * de formato y longitud.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-10
 */
@Getter
public class CreateParticipanCompdestUserDTO extends CreateCompdesUserDTO {

    @NotBlank(message = "Debe ingresar el número del documento de identificación")
    @Size(max = 30, message = "El documento de identificación no puede exceder los 30 caracteres")
    private String identificationDocument;

    public CreateParticipanCompdestUserDTO(
            @NotBlank(message = "Por favor, ingresa un nombre de usuario.") @Size(max = 50, message = "El nombre de usuario debe tener como máximo 50 caracteres.") String username,
            @NotBlank(message = "Por favor, ingresa una contraseña.") @Size(max = 100, message = "La contraseña no debe superar los 100 caracteres.") String password,
            @NotBlank(message = "Debe ingresar el número del documento de identificación") @Size(max = 30, message = "El documento de identificación no puede exceder los 30 caracteres") String identificationDocument) {
        super(username, password);
        this.identificationDocument = identificationDocument;
    }

}