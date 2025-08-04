package com.compdes.reservations.models.dto.response;

import java.time.LocalDateTime;

import com.compdes.participants.models.dto.response.AdminParticipantProfileDTO;

import lombok.Value;

/**
 * DTO utilizado para mandar los datos necesarios para la visualización de
 * todos los talleres reservados para un participante en particular.
 *
 * Esta clase es utilizada en peticiones HTTP para transportar
 * la información básica de talleres reservados.
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-07-21
 */
@Value
public class ReservationParticipantsDTO {
    private AdminParticipantProfileDTO participant;
    private LocalDateTime attendedDateTime;
}
