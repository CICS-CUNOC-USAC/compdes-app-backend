package com.compdes.participants.models.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * DTO utilizado para crear participantes desde el panel de administración.
 * 
 * Este objeto extiende de CreateParticipantDTO y agrega campos específicos
 * relacionados con el rol de autor y la validación del comprobante de pago.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-04
 */
@Getter
public class CreateParticipantByAdminDTO extends CreateParticipantDTO {

    @NotNull(message = "Debes indicar si la persona es autor.")
    private Boolean isAuthor;

    @Size(max = 50, message = "El número de comprobante no puede tener más de 50 caracteres.")
    private String voucherNumber;

    public CreateParticipantByAdminDTO(String firstName, String lastName, String email, String phone,
            String organisation, String identificationDocument,
            @NotNull(message = "Debes indicar si la persona es autor.") Boolean isAuthor,
            @Size(max = 50, message = "El número de comprobante no puede tener más de 50 caracteres.") String voucherNumber) {
        super(firstName, lastName, email, phone, organisation, identificationDocument);
        this.isAuthor = isAuthor;
        this.voucherNumber = voucherNumber;
    }

    @AssertTrue(message = "El número de comprobante es obligatorio si el participante no es autor, y debe tener como máximo 50 caracteres.")
    public boolean isVoucherValid() {
        if (Boolean.FALSE.equals(isAuthor)) {
            return voucherNumber != null;
        }
        return true;
    }
}
