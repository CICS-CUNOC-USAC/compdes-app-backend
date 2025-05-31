package com.compdes.registrationStatus.models.entities;

import org.hibernate.annotations.DynamicUpdate;

import com.compdes.common.models.entities.Auditor;
import com.compdes.participants.models.entities.Participant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Representa el estado del registro de un participante.
 * 
 * Esta entidad contiene información sobre si el registro fue aprobado
 * y el método de pago utilizado (efectivo u otro). Permite llevar control
 * sobre la validación del registro y el tipo de transacción asociada.
 * 
 * Hereda de {@link Auditor} para incluir trazabilidad de creación y
 * modificación.
 * 
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-30
 */
@Entity
@DynamicUpdate
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class RegistrationStatus extends Auditor {

    @OneToOne
    @JoinColumn(nullable = false, unique = true)
    private Participant participant;

    @Column(nullable = false)
    private Boolean isApproved;

    @Column(nullable = false)
    private Boolean isCashPayment;

    /**
     * Constructor utilizado para crear y persistir un nuevo estado de registro
     * asociado a un participante.
     *
     * @param participant   participante al que pertenece este estado de registro
     * @param isApproved    indica si el registro fue aprobado
     * @param isCashPayment indica si el pago fue realizado en efectivo
     */
    public RegistrationStatus(Participant participant, Boolean isApproved, Boolean isCashPayment) {
        this.participant = participant;
        this.isApproved = isApproved;
        this.isCashPayment = isCashPayment;
    }

}
