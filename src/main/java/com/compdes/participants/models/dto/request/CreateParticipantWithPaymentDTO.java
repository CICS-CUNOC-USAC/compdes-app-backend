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
public class CreateParticipantWithPaymentDTO extends CreateParticipantDTO {

    @Valid
    private CreatePaymentProofDTO paymentProof;

    public CreateParticipantWithPaymentDTO(String firstName, String lastName, String email, String phone,
            String organisation, String identificationDocument, Boolean isAuthor,
            @Valid CreatePaymentProofDTO paymentProof) {
        super(firstName, lastName, email, phone, organisation, identificationDocument, isAuthor);
        this.paymentProof = paymentProof;
    }

}
