package com.compdes.participants.models.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO utilizado para recibir los datos necesarios para la creación de un
 * participante.
 * 
 * Esta clase es utilizada en peticiones HTTP para validar y transportar
 * la información básica de un nuevo participante del sistema, incluyendo
 * datos personales, de contacto y de identificación.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-01
 */
@Data
@AllArgsConstructor
public class CreateParticipantDTO {

    @NotBlank(message = "Debe ingresar el nombre del participante")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String firstName;

    @NotBlank(message = "Debe ingresar el apellido del participante")
    @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres")
    private String lastName;

    @NotBlank(message = "Debe proporcionar un correo electrónico")
    @Email(message = "El correo electrónico no tiene un formato válido")
    @Size(max = 100, message = "El correo electrónico no puede exceder los 100 caracteres")
    private String email;

    @NotBlank(message = "Debe ingresar un número de teléfono")
    @Size(max = 20, message = "El número de teléfono no puede exceder los 20 caracteres")
    private String phone;

    @NotBlank(message = "Debe indicar la organización a la que pertenece")
    @Size(max = 100, message = "El nombre de la organización no puede exceder los 100 caracteres")
    private String organisation;

    @NotBlank(message = "Debe ingresar el número del documento de identificación")
    @Size(max = 30, message = "El documento de identificación no puede exceder los 30 caracteres")
    private String identificationDocument;

}