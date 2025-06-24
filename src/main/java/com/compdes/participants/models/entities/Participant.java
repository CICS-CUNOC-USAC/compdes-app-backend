package com.compdes.participants.models.entities;

import org.hibernate.annotations.DynamicUpdate;

import com.compdes.auth.users.models.entities.CompdesUser;
import com.compdes.common.exceptions.QrCodeException;
import com.compdes.common.exceptions.enums.QrCodeErrorEnum;
import com.compdes.common.models.entities.Auditor;
import com.compdes.participants.models.dto.request.UpdateParticipantByAdminDTO;
import com.compdes.paymentProofs.models.entities.PaymentProof;
import com.compdes.qrCodes.models.entities.QrCode;
import com.compdes.registrationStatus.models.entities.RegistrationStatus;
import com.compdes.storedFiles.models.entities.StoredFile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Getter
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

    @Column(nullable = false)
    private Boolean isGuest;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(nullable = true)
    private PaymentProof paymentProof;

    @OneToOne
    @JoinColumn(nullable = true)
    @Setter(value = AccessLevel.NONE)
    private QrCode qrCode;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private RegistrationStatus registrationStatus;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(nullable = true)
    private StoredFile paymentProofImage;

    @OneToOne
    @JoinColumn(nullable = true)
    @Setter(value = AccessLevel.NONE)
    private CompdesUser compdesUser;

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
            RegistrationStatus registrationStatus, StoredFile paymentProofImage, Boolean isGuest) {
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
        this.paymentProofImage = paymentProofImage;
        this.isGuest = isGuest;
    }

    /**
     * Actualiza los datos del participante con los valores proporcionados.
     * 
     * @param dto objeto con los datos actualizados del participante
     */
    public void update(UpdateParticipantByAdminDTO dto) {
        // actualizacion de campos personales
        setFirstName(dto.getFirstName());
        setLastName(dto.getLastName());
        setEmail(dto.getEmail());
        setPhone(dto.getPhone());
        setOrganisation(dto.getOrganisation());
        setIdentificationDocument(dto.getIdentificationDocument());
        setIsAuthor(dto.getIsAuthor());        
    }

    /**
     * Retorna el nombre completo del participante.
     * 
     * Concatena el nombre y el apellido separados por un espacio.
     * 
     * @return el nombre completo en formato "Nombre Apellido"
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * Asocia un usuario del sistema al participante actual.
     * 
     * Este método asigna la instancia de {@link CompdesUser} al participante,
     * siempre que no tenga ya un usuario asociado previamente.
     * 
     * Si el participante ya tiene un usuario, lanza una excepción para prevenir
     * sobrescritura no intencionada.
     * 
     * @param compdesUser el usuario del sistema que se desea asociar
     * @throws IllegalStateException si el participante ya tiene un usuario asociado
     */
    public void setCompdesUser(CompdesUser compdesUser) {
        if (this.compdesUser != null) {
            throw new IllegalStateException(
                    "El participante ya tiene un usuario asociado y no se puede asignar uno nuevo.");
        }
        this.compdesUser = compdesUser;
    }

    /**
     * Establece la relación entre este participante y un {@link QrCode}.
     * 
     * Este método asigna un código QR al participante, siempre que no tenga ya uno
     * previamente asociado. Si ya existe una relación con un QR, se lanza una
     * excepción para evitar la sobrescritura no permitida.
     * 
     * Lanza una instancia de {@link QrCodeException} definida en
     * {@link QrCodeErrorEnum#PARTICIPANT_ALREADY_HAS_QR} si el participante ya
     * tiene un código QR asignado.
     * 
     * @param qrCode el código QR que se desea asociar
     * @throws QrCodeException si el participante ya tiene un código QR asociado
     */
    public void setQrCode(QrCode qrCode) {
        if (this.qrCode != null) {
            throw QrCodeErrorEnum.PARTICIPANT_ALREADY_HAS_QR.getQrCodeException();
        }
        this.qrCode = qrCode;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public void setPhone(String phone) {
        this.phone = phone.trim();
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation.trim();
    }

    public void setIdentificationDocument(String identificationDocument) {
        this.identificationDocument = identificationDocument.trim();
    }

    public void setIsAuthor(Boolean isAuthor) {
        this.isAuthor = isAuthor;
    }

    public void setIsGuest(Boolean isGuest) {
        this.isGuest = isGuest;
    }

    public void setPaymentProof(PaymentProof paymentProof) {
        this.paymentProof = paymentProof;
    }

    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public void setPaymentProofImage(StoredFile paymentProofImage) {
        this.paymentProofImage = paymentProofImage;
    }

}
