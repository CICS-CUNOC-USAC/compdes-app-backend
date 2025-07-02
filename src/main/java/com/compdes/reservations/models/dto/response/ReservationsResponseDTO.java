package com.compdes.reservations.models.dto.response;

import com.compdes.activity.models.dto.response.ActivityDTO;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * DTO utilizado para mandar los datos necesarios para la visualización de
 * todos los talleres reservados para un participante en particular.
 *
 * Esta clase es utilizada en peticiones HTTP para transportar
 * la información básica de talleres reservados.
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-06-03
 */
@Value
public class ReservationsResponseDTO {
    private ActivityDTO activityDTO;
    private LocalDateTime attendedDatetime;
}
