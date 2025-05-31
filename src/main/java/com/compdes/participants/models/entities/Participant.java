package com.compdes.participants.models.entities;

import org.hibernate.annotations.DynamicUpdate;

import com.compdes.common.models.entities.Auditor;
import com.compdes.paymentProofs.models.entities.PaymentProof;
import com.compdes.qrCodes.models.entities.QrCode;
import com.compdes.registrationStatus.models.entities.RegistrationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Representa a un participante registrado en el sistema.
 * 
 * Esta entidad almacena la información personal, organizacional y de
 * inscripción
 * del participante, incluyendo su comprobante de pago
 * código QR y estado de registro.
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
public class Participant extends Auditor {

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 100)
    private String organisation;

    @Column(nullable = false, length = 30, unique = true)
    private String identificationDocument;

    @Column(nullable = false)
    private Boolean isAuthor;

    @OneToOne
    @JoinColumn(nullable = true)
    private PaymentProof paymentProof;

    @OneToOne
    @JoinColumn(nullable = true)
    private QrCode qrCode;

    @OneToOne
    private RegistrationStatus registrationStatus;

    /**
     * Constructor utilizado para crear y persistir un nuevo participante por
     * primera vez.
     *
     * @param firstName              nombre del participante
     * @param lastName               apellido del participante
     * @param email                  correo electrónico del participante
     * @param phone                  número de teléfono del participante
     * @param organisation           organización a la que pertenece el participante
     * @param identificationDocument documento de identificación del participante
     * @param isAuthor               indica si el participante es autor
     * @param paymentProof           comprobante de pago asociado al participante
     * @param qrCode                 código QR generado para el participante (puede
     *                               ser nulo)
     * @param registrationStatus     estado de registro del participante
     */
    public Participant(String firstName, String lastName, String email, String phone, String organisation,
            String identificationDocument, Boolean isAuthor, PaymentProof paymentProof, QrCode qrCode,
            RegistrationStatus registrationStatus) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.organisation = organisation;
        this.identificationDocument = identificationDocument;
        this.isAuthor = isAuthor;
        this.paymentProof = paymentProof;
        this.qrCode = qrCode;
        this.registrationStatus = registrationStatus;
    }

}
