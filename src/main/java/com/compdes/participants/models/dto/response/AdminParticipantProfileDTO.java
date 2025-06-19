package com.compdes.participants.models.dto.response;

import com.compdes.registrationStatus.models.dto.response.PrivateRegistrationStatusInfoDTO;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO que representa información privada y completa de un participante
 * registrado en el sistema.
 * 
 * Esta clase está diseñada para ser utilizada únicamente en contextos
 * autenticados donde se requiere
 * acceso total a los datos del participante. No debe utilizarse en endpoints
 * públicos, ya que expone
 * información personal crítica.
 * 
 * Se utiliza comúnmente para la visualización en paneles administrativos o en
 * áreas seguras del sistema,
 * como parte de funcionalidades de revisión, seguimiento o gestión de
 * inscripciones.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-12
 */
@Getter
@Setter
public class AdminParticipantProfileDTO extends ParticipantProfileDTO {

    private String id;
    private PrivateRegistrationStatusInfoDTO registrationStatus;
    private Boolean isTransferPayment;
    private Boolean isCardPayment;
    private String cardPaymentProofLink;
    private String transferPaymentProofLink;
    private String createdAt;

    public AdminParticipantProfileDTO(String firstName, String lastName, String organisation, Boolean isAuthor,
            Boolean isGuest, String email, String phone, String identificationDocument, String qrCodeLink, String id,
            PrivateRegistrationStatusInfoDTO registrationStatus, Boolean isTransferPayment, Boolean isCardPayment,
            String cardPaymentProofLink, String transferPaymentProofLink, String createdAt) {
        super(firstName, lastName, organisation, isAuthor, isGuest, email, phone, identificationDocument, qrCodeLink);
        this.id = id;
        this.registrationStatus = registrationStatus;
        this.isTransferPayment = isTransferPayment;
        this.isCardPayment = isCardPayment;
        this.cardPaymentProofLink = cardPaymentProofLink;
        this.transferPaymentProofLink = transferPaymentProofLink;
        this.createdAt = createdAt;
    }

}
