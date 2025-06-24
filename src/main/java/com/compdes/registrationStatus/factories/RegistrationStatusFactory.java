package com.compdes.registrationStatus.factories;

import org.springframework.stereotype.Component;

import com.compdes.registrationStatus.models.entities.RegistrationStatus;

/**
 * Fábrica para crear instancias de {@link RegistrationStatus} con
 * configuraciones predeterminadas.
 * 
 * Esta clase encapsula la lógica de inicialización de estados de registro,
 * tanto para registros
 * públicos como para registros administrativos con parámetros personalizados.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-23
 */
@Component
public class RegistrationStatusFactory {

    /**
     * Crea un estado de registro por defecto para inscripciones públicas.
     * 
     * El estado se genera sin aprobación, sin pago en efectivo y sin número de
     * comprobante.
     * 
     * @return una instancia nueva de {@link RegistrationStatus} con valores
     *         predeterminados
     */
    public RegistrationStatus createDefaultForPublic() {
        return RegistrationStatus.builder()
                .isApproved(false)
                .isCashPayment(false)
                .voucherNumber(null)
                .build();
    }

    /**
     * Crea un estado de registro basado en entrada administrativa.
     * 
     * Si el participante es invitado, no se asigna tipo de pago ni número de
     * comprobante.
     * Si no es invitado, se marca como pago en efectivo y se incluye el número de
     * comprobante.
     * 
     * @param isGuest       indica si el participante es invitado (sin pago)
     * @param voucherNumber número de comprobante a registrar (si aplica)
     * @return una instancia nueva de {@link RegistrationStatus} configurada según
     *         el tipo de participante
     */
    public RegistrationStatus fromAdminInput(Boolean isGuest, String voucherNumber) {
        return RegistrationStatus.builder()
                .isApproved(false)
                .isCashPayment(isGuest ? null : true) // indica si el pago fue en efectivo solo si no es invitado
                                                      // (invitado no paga)
                .voucherNumber(isGuest ? null : voucherNumber) // asigna un numero de comprobante si no es guest
                .build();
    }
}
