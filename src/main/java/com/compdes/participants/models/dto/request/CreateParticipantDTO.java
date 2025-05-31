package com.compdes.participants.models.dto.request;

import com.compdes.paymentProofs.models.dto.request.CreatePaymentProofDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-30
 */
@Getter
@Setter
@AllArgsConstructor
public class CreateParticipantDTO {

    @NotBlank(message = "Debe ingresar el nombre del participante")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    String firstName;

    @NotBlank(message = "Debe ingresar el apellido del participante")
    @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres")
    String lastName;

    @NotBlank(message = "Debe proporcionar un correo electrónico")
    @Email(message = "El correo electrónico no tiene un formato válido")
    @Size(max = 100, message = "El correo electrónico no puede exceder los 100 caracteres")
    String email;

    @NotBlank(message = "Debe ingresar un número de teléfono")
    @Size(max = 20, message = "El número de teléfono no puede exceder los 20 caracteres")
    String phone;

    @NotBlank(message = "Debe indicar la organización a la que pertenece")
    @Size(max = 100, message = "El nombre de la organización no puede exceder los 100 caracteres")
    String organisation;

    @NotBlank(message = "Debe ingresar el número del documento de identificación")
    @Size(max = 30, message = "El documento de identificación no puede exceder los 30 caracteres")
    String identificationDocument;

    @Valid
    CreatePaymentProofDTO paymentProof;

}
