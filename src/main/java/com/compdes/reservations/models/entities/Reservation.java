package com.compdes.reservations.models.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicUpdate;

import com.compdes.activity.models.entities.Activity;
import com.compdes.common.models.entities.Auditor;
import com.compdes.participants.models.entities.Participant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Representa una reservacion de un taller registrado en el sistema.
 *
 *
 * Hereda de {@link Auditor} para incluir trazabilidad de creación y
 * modificación.
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-07-01
 */
@Entity
@DynamicUpdate
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Reservation extends Auditor{

    @ManyToOne
    @JoinColumn(nullable = false)
    private Participant participant;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Activity activity;

    @Column(nullable = true)
    private LocalDateTime attendedDateTime;

    /**
     * Constructor utilizado para crear y persistir una nueva reservacion por
     * primera vez.
     *
     */
    public Reservation(Participant participant, Activity activity) {
        this.participant = participant;
        this.activity = activity;
    }


}
