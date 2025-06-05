package com.compdes.qrCodes.models.entities;

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
 * Representa un código QR asignado a un participante.
 * 
 * Esta entidad almacena un número único utilizado para generar
 * un código QR, que puede ser utilizado para validación, registro o control
 * de acceso en el contexto del evento.
 * 
 * La relación con el participante es opcional para permitir la generación
 * previa
 * de códigos QR antes de la asignación directa.
 * 
 * Hereda de {@link Auditor} para incluir trazabilidad de creación y
 * modificación.
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
public class QrCode extends Auditor {

    @OneToOne
    @JoinColumn(nullable = true)
    private Participant participant;

    @Column(nullable = false, unique = true)
    private Integer numberCode;

    /**
     * Constructor utilizado para crear y persistir un nuevo código QR asociado a un
     * participante.
     *
     * @param participant participante al que se asigna el código QR (puede ser nulo)
     * @param numberCode  código numérico único que representa el contenido del
     *                    código QR
     */
    public QrCode(Participant participant, Integer numberCode) {
        this.participant = participant;
        this.numberCode = numberCode;
    }

}
