package com.compdes.participants.models.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @NotNull(message = "Debes indicar si la persona es invitada o no")
    private Boolean isGuest;

    @Size(max = 50, message = "El número de comprobante no puede tener más de 50 caracteres")
    private String voucherNumber;

    public CreateParticipantByAdminDTO(String firstName, String lastName, String email, String phone,
            String organisation, String identificationDocument, Boolean isAuthor,
            @NotNull(message = "Debes indicar si la persona es invitada o no") Boolean isGuest,
            @Size(max = 50, message = "El número de comprobante no puede tener más de 50 caracteres") String voucherNumber) {
        super(firstName, lastName, email, phone, organisation, identificationDocument, isAuthor);
        this.isGuest = isGuest;
        this.voucherNumber = voucherNumber;
    }

    /**
     * Valida la consistencia entre el tipo de participante y la presencia del
     * número de comprobante.
     * 
     * Este método se utiliza como una regla de validación condicional: si el
     * participante
     * no es invitado (<code>isGuest == false</code>), entonces el campo
     * <code>voucherNumber</code>
     * debe estar presente. Si esta condición no se cumple, se produce un error de
     * validación.
     * 
     * Aunque el método es interpretado como una propiedad booleana, no representa
     * un campo real
     * y está oculto en la documentación de OpenAPI mediante
     * {@link Schema#hidden()}.
     * 
     * @return <code>true</code> si la validación es consistente; <code>false</code>
     *         si falta el número de comprobante cuando se requiere
     */
    @AssertTrue(message = "El número de comprobante debe proporcionarse cuando el participante no es invitado.")
    @Schema(hidden = true)
    public boolean isVoucherValid() {
        if (Boolean.FALSE.equals(isGuest)) {
            return voucherNumber != null;
        }
        return true;
    }
}
