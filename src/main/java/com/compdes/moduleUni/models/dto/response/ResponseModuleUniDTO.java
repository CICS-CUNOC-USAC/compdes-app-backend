package com.compdes.moduleUni.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * DTO utilizado para mandar los datos necesarios para la visualizacion de la lista de salones
 *
 * Esta clase es utilizada en peticiones HTTP para transportar
 * la información básica de un nuevo salon del sistema
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-06-05
 */
@Value
public class ResponseModuleUniDTO {
    private String id;
    private String name;
}
