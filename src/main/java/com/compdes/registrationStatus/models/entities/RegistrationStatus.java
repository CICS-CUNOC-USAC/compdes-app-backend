package com.compdes.registrationStatus.models.entities;

import org.hibernate.annotations.DynamicUpdate;

import com.compdes.common.models.entities.Auditor;
import com.compdes.participants.models.entities.Participant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa el estado del registro de un participante.
 * 
 * Esta entidad contiene información sobre si el registro fue aprobado
 * y el método de pago utilizado (efectivo u otro). Permite llevar control
 * sobre la validación del registro y el tipo de transacción asociada.
 * 
 * Nota: La bandera {@code isCashPayment} puede ser nula si se trata de un autor
 * ya que los autores no pagan la inscripcion.
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

    @OneToOne(mappedBy = "registrationStatus")
    private Participant participant;

    @Column(nullable = false)
    private Boolean isApproved;

    @Column(nullable = true)
    private Boolean isCashPayment;

    @Column(nullable = true, unique = true, length = 10)
    @Setter(value = AccessLevel.PRIVATE)
    private String voucherNumber;

    /**
     * Constructor utilizado para crear y persistir un nuevo estado de registro
     * asociado a un participante.
     *
     * @param participant   participante al que pertenece este estado de registro
     * @param isApproved    indica si el registro fue aprobado
     * @param isCashPayment indica si el pago fue realizado en efectivo
     */
    @Builder
    public RegistrationStatus(Participant participant, Boolean isApproved, Boolean isCashPayment,
            String voucherNumber) {
        this.participant = participant;
        this.isApproved = isApproved;
        this.isCashPayment = isCashPayment;
        this.voucherNumber = voucherNumber;
    }

    /**
     * Asigna un nuevo número de comprobante al estado de registro.
     * 
     * Este método actualiza el campo {@code voucherNumber} únicamente si el
     * participante realizó un pago en efectivo. Si el campo {@code isCashPayment}
     * es falso o nulo, se lanza una excepción para evitar una asignación inválida.
     * 
     * @param voucherNumber nuevo número de comprobante a asignar
     * @throws IllegalArgumentException si el pago no fue en efectivo o esta nulo
     */
    public void updateVoucherNumber(String voucherNumber) {
        if (isCashPayment == null) {
            throw new IllegalArgumentException(
                    "Este participante está registrado como invitado y no realizó ningún pago, por lo tanto, no se le puede asignar un número de talonario.");
        }

        if (!isCashPayment) {
            throw new IllegalArgumentException(
                    "Parece que este participante no realizó un pago en efectivo, por lo tanto, no se le puede asignar un número de talonario.");
        }

        if (isCashPayment && voucherNumber == null) {
            throw new IllegalArgumentException(
                    "Este participante realizó un pago en efectivo, por lo tanto, es necesario asignar un número de talonario.");
        }
        setVoucherNumber(voucherNumber);
    }

    /**
     * Aprueba el estado de registro si aún no ha sido aprobado.
     * 
     * Este método valida si el estado ya fue aprobado previamente. En caso de que
     * ya esté aprobado, lanza una excepción para evitar una operación redundante
     * o inconsistente.
     * 
     * @throws IllegalStateException si el estado de registro ya ha sido aprobado
     *                               previamente
     */
    public void approve() {
        if (isApproved) {
            throw new IllegalStateException("El estado de registro ya ha sido aprobado previamente.");
        }
        isApproved = true;
    }

}
