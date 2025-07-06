package com.compdes.reservations.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

/**
 * DTO utilizado para recibir los datos necesarios para registrar la asistencia
 * de un taller a un participante
 *
 * Esta clase es utilizada en peticiones HTTP para validar y transportar
 * la información básica de una asistencia a un taller
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-07-05
 */
@Value
public class AssistanceToReservationDTO {
    @NotBlank(message = "El identificador del qr no puede estar vacio")
    @Size(min = 36, max = 36, message = "identificador de qr invalido")
    private String qrId;

    @NotBlank(message = "Debe de seleccionar un taller valido")
    @Size(min = 36, max = 36, message = "identificador de taller invalido")
    private String activityId;

}
