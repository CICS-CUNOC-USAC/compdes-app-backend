package com.compdes.reservations.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

/**
 * DTO utilizado para recibir los datos necesarios para reservar un lugar en un taller
 *
 * Esta clase es utilizada en peticiones HTTP para validar y transportar
 * la información básica de una reservacion a un taller
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-07-01
 */
@Value
public class ReservationDTO {
    @NotBlank(message = "Debe de seleccionar un taller valido")
    @Size(min = 36, max = 36, message = "identificador de taller invalido")
    private String activityId;

}
