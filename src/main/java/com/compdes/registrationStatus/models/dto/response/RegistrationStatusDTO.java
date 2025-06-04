package com.compdes.registrationStatus.models.dto.response;

import lombok.Value;

/**
 * DTO que representa el estado de registro de un participante.
 * Contiene información sobre si el registro fue aprobado y
 * si el método de pago fue en efectivo.
 *
 * Esta clase es inmutable y utiliza Lombok para generar automáticamente
 * constructor, getters, equals, hashCode y toString.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-03
 */
@Value
public class RegistrationStatusDTO {

    String id;
    Boolean isApproved;
    Boolean isCashPayment;
}
