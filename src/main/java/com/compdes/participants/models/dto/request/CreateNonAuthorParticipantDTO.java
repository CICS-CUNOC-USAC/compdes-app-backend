package com.compdes.participants.models.dto.request;

import com.compdes.paymentProofs.models.dto.request.CreatePaymentProofDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-30
 */
@Getter
public class CreateNonAuthorParticipantDTO extends CreateParticipantDTO {

    @Valid
    @NotNull(message = "Debe proporcionar la prueba de pago del participante")
    CreatePaymentProofDTO paymentProof;

    public CreateNonAuthorParticipantDTO(String firstName, String lastName, String email, String phone,
            String organisation, String identificationDocument, CreatePaymentProofDTO paymentProof) {
        super(firstName, lastName, email, phone, organisation, identificationDocument);
        this.paymentProof = paymentProof;
    }

}
