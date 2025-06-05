package com.compdes.participants.models.dto.response;

import com.compdes.registrationStatus.models.dto.response.RegistrationStatusDTO;

import lombok.Value;

/**
 * DTO que representa a un participante registrado.
 * Contiene información personal, de contacto y el estado de su registro.
 *
 * Esta clase es inmutable y utiliza Lombok para generar automáticamente
 * constructor, getters, equals, hashCode y toString.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-03
 */
@Value
public class ParticipantDTO {
    
    String id;
    String firstName;
    String lastName;
    String email;
    String phone;
    String organisation;
    String identificationDocument;
    Boolean isAuthor;
    RegistrationStatusDTO registrationStatus;

}
