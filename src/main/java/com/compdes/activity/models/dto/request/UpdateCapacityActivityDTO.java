package com.compdes.activity.models.dto.request;

import lombok.Value;

/**
 * DTO utilizado para recibir los datos necesarios para la creación de un
 * salon de conferencias.
 *
 * Esta clase es utilizada en peticiones HTTP para validar y transportar
 * la información básica de un nuevo salon del sistema
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-07-21
 */
@Value
public class UpdateCapacityActivityDTO {
    private Integer capacity;
}
