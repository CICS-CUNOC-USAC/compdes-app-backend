package com.compdes.paymentProofs.models.entities;

import org.hibernate.annotations.DynamicUpdate;

import com.compdes.common.models.entities.Auditor;
import com.compdes.participants.models.entities.Participant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Representa una constancia de pago asociada a un participante.
 * 
 * Esta entidad almacena el enlace a un documento que sirve como evidencia
 * de que el participante ha realizado el pago correspondiente a su registro.
 * 
 * Hereda de {@link Auditor} para incluir trazabilidad de creación y
 * modificación.
 * 
 * Cada constancia está relacionada uno a uno con un participante.
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
public class PaymentProof extends Auditor {

    @OneToOne(mappedBy = "paymentProof")
    private Participant participant;

    @Column(length = 100, nullable = false)
    private String link;

    /**
     * Constructor utilizado normalmente al registrar y persistir una nueva
     * constancia de pago.
     *
     * @param participant participante al que pertenece la constancia
     * @param link        enlace al documento o evidencia de pago
     */
    public PaymentProof(Participant participant, String link) {
        this.participant = participant;
        this.link = link;
    }

}
