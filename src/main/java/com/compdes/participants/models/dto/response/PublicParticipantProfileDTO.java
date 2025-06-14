package com.compdes.participants.models.dto.response;

import com.compdes.registrationStatus.models.dto.response.PublicRegistrationStatusInfoDTO;

import lombok.Getter;

/**
 * DTO que representa información pública de un participante.
 * 
 * Este DTO está diseñado para ser utilizado en endpoints accesibles sin
 * autenticación,
 * donde se requiere mostrar datos generales del participante y el estado de su
 * inscripción,
 * sin comprometer información personal sensible.
 * 
 * Incluye nombre, apellido, organización, si el participante es autor o
 * invitado,
 * y el estado público del registro.
 * 
 * Su uso principal es en consultas abiertas por número de documento de
 * identificación.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-12
 */
@Getter
public class PublicParticipantProfileDTO extends BaseParticipantInfoDTO {

    private PublicRegistrationStatusInfoDTO registrationStatus;

    public PublicParticipantProfileDTO(String firstName, String lastName, String organisation, Boolean isAuthor,
            Boolean isGuest, PublicRegistrationStatusInfoDTO registrationStatus) {
        super(firstName, lastName, organisation, isAuthor, isGuest);
        this.registrationStatus = registrationStatus;
    }

}
