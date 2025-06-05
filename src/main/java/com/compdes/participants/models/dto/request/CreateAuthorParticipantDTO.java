package com.compdes.participants.models.dto.request;

import lombok.Getter;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-30
 */
@Getter
public class CreateAuthorParticipantDTO extends CreateParticipantDTO {

    public CreateAuthorParticipantDTO(String firstName, String lastName, String email, String phone,
            String organisation, String identificationDocument) {
        super(firstName, lastName, email, phone, organisation, identificationDocument);
    }
}
