package com.compdes.reservations.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class CountParticipantsDTO {
    private long total;
}
