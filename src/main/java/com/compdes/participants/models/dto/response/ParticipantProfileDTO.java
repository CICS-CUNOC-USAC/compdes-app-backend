package com.compdes.participants.models.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO que representa el perfil completo de un participante.
 * 
 * <p>
 * Este DTO está diseñado para ser expuesto únicamente a usuarios con rol
 * de **PARTICIPANT** o **ADMIN**, ya que contiene información sensible. No debe
 * ser utilizado en endpoints públicos o sin autenticación/autorización
 * adecuada.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-13
 */
@Getter
@Setter
public class ParticipantProfileDTO extends BaseParticipantInfoDTO {

    private String email;
    private String phone;
    private String identificationDocument;
    private String qrCodeLink;

    public ParticipantProfileDTO(String firstName, String lastName, String organisation, Boolean isAuthor,
            Boolean isGuest, String email, String phone, String identificationDocument, String qrCodeLink) {
        super(firstName, lastName, organisation, isAuthor, isGuest);
        this.email = email;
        this.phone = phone;
        this.identificationDocument = identificationDocument;
        this.qrCodeLink = qrCodeLink;
    }

}
