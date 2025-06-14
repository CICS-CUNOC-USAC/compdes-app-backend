package com.compdes.registrationStatus.models.dto.response;

import lombok.Getter;

/**
 * DTO que representa información pública del estado de registro de un
 * participante.
 * 
 * Este DTO expone únicamente si el registro fue aprobado o no, sin incluir
 * detalles
 * sensibles como el identificador del registro ni el método de pago. Está
 * diseñado
 * para ser utilizado en endpoints públicos donde se desea mostrar el estado de
 * aprobación sin comprometer la privacidad del participante.
 * 
 * Hereda la propiedad `isApproved` desde {@link BaseRegistrationStatusInfoDTO}.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-12
 */
@Getter
public class PublicRegistrationStatusInfoDTO extends BaseRegistrationStatusInfoDTO {

    public PublicRegistrationStatusInfoDTO(Boolean isApproved) {
        super(isApproved);
    }
}
