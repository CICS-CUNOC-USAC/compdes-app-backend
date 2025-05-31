package com.compdes.common.models.dto.response;

import lombok.Value;

/**
 * Objeto de transferencia utilizado para representar respuestas de error
 * de forma estructurada en la API.
 * 
 * Esta clase encapsula un mensaje de error que puede ser devuelto al cliente
 * cuando ocurre una excepción o falla en la validación.
 * 
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-31
 */
@Value
public class ErrorDTO {

    String message;
}
