package com.compdes.registrationStatus.models.dto.response;

import lombok.Getter;

/**
 * DTO base que representa la información común del estado de registro de un
 * participante.
 * 
 * Contiene el campo {@code isApproved}, que indica si el registro ha sido
 * aprobado por la administración.
 * Esta clase sirve como superclase para estructuras públicas o privadas que
 * extiendan la información del estado de registro.
 * 
 * Puede ser utilizada en contextos donde únicamente se necesita conocer si un
 * participante está aprobado,
 * y se desee ocultar detalles adicionales como métodos de pago o
 * identificadores internos.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-12
 */
@Getter
public abstract class BaseRegistrationStatusInfoDTO {

    private Boolean isApproved;

    public BaseRegistrationStatusInfoDTO(Boolean isApproved) {
        this.isApproved = isApproved;
    }

}
