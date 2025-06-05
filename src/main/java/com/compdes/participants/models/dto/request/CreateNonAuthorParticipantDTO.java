package com.compdes.participants.models.dto.request;

import com.compdes.paymentProofs.models.dto.request.CreatePaymentProofDTO;

import jakarta.validation.Valid;
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
public class CreateNonAuthorParticipantDTO extends CreateParticipantDTO {

    @Valid
    CreatePaymentProofDTO paymentProof;

    public CreateNonAuthorParticipantDTO(String firstName, String lastName, String email, String phone,
            String organisation, String identificationDocument, CreatePaymentProofDTO paymentProof) {
        super(firstName, lastName, email, phone, organisation, identificationDocument);
        this.paymentProof = paymentProof;
    }

}
