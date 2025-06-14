package com.compdes.participants.models.dto.response;

import lombok.Getter;

/**
 * DTO base que representa información general de un participante.
 * 
 * Contiene los campos comunes a todas las vistas del participante, tanto
 * públicas como privadas,
 * incluyendo nombre, apellido, organización y banderas de autoría o
 * participación especial.
 * 
 * Esta clase sirve como superclase para derivaciones que requieren mostrar más
 * o menos
 * información según el contexto (por ejemplo, información pública o privada).
 * 
 * No debe usarse directamente como respuesta a endpoints; está diseñada para
 * ser extendida.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-14
 */
@Getter
public abstract class BaseParticipantInfoDTO {

    private String firstName;
    private String lastName;
    private String organisation;
    private Boolean isAuthor;
    private Boolean isGuest;

    public BaseParticipantInfoDTO(String firstName, String lastName, String organisation, Boolean isAuthor,
            Boolean isGuest) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.organisation = organisation;
        this.isAuthor = isAuthor;
        this.isGuest = isGuest;
    }

}
