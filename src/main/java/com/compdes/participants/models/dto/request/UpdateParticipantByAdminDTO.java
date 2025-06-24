package com.compdes.participants.models.dto.request;

import com.compdes.paymentProofs.models.dto.request.UpdatePaymentProofByAdminDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * DTO utilizado para actualizar la información de un participante desde el
 * panel de administración.
 * 
 * Extiende {@link CreateParticipantDTO} e incorpora campos adicionales
 * relacionados al pago, modificar el número de comprobante y el enlace al
 * comprobante de pago.
 * 
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-22
 */
@Getter
public class UpdateParticipantByAdminDTO extends CreateParticipantDTO {

    @Size(max = 50, message = "El número de comprobante no puede tener más de 50 caracteres")
    @Schema(description = "Número de comprobante de pago (opcional)", required = false)
    private String voucherNumber;

    @Valid
    private UpdatePaymentProofByAdminDTO paymentProof;

    public UpdateParticipantByAdminDTO(String firstName, String lastName, String email, String phone,
            String organisation, String identificationDocument, Boolean isAuthor, String voucherNumber,
            @Valid UpdatePaymentProofByAdminDTO paymentProof) {
        super(firstName, lastName, email, phone, organisation, identificationDocument, isAuthor);
        this.voucherNumber = voucherNumber;
        this.paymentProof = paymentProof;
    }

    /**
     * Valida que solo se proporcione una opción de comprobante (voucher o pago con
     * tarjeta),
     * o ninguna (ambas nulas).
     * 
     * @return true si solo una opción está presente o si ambas son nulas
     */
    @AssertTrue(message = "Solo se debe proporcionar una opción de comprobante: número de comprobante o pago con tarjeta.")
    @Schema(hidden = true)
    public boolean isOnlyOneProofPresent() {
        return !(voucherNumber != null && paymentProof != null);
    }

}
