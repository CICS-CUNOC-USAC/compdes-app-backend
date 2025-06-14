package com.compdes.registrationStatus.models.dto.response;

import lombok.Getter;

/**
 * DTO que representa información privada del estado de registro de un
 * participante.
 * 
 * A diferencia de su contraparte pública, este DTO expone información sensible.
 * 
 * Este DTO debe ser utilizado únicamente en contextos autenticados o internos
 * donde
 * se requiera una visión completa del estado de registro del participante.
 * 
 * Hereda los campos públicos desde {@link BaseRegistrationStatusInfoDTO},
 * agregando
 * detalles adicionales relevantes para la gestión interna del sistema.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-03
 */
@Getter
public class PrivateRegistrationStatusInfoDTO extends BaseRegistrationStatusInfoDTO {

    private String id;
    private Boolean isCashPayment;
    private String voucherNumber;

    public PrivateRegistrationStatusInfoDTO(Boolean isApproved, String id, Boolean isCashPayment,
            String voucherNumber) {
        super(isApproved);
        this.id = id;
        this.isCashPayment = isCashPayment;
        this.voucherNumber = voucherNumber;
    }

}
