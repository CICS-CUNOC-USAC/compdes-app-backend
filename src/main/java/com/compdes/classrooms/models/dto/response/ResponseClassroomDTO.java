package com.compdes.classrooms.models.dto.response;

import com.compdes.moduleUni.models.dto.response.ResponseModuleUniDTO;

import lombok.Value;

/**
 * DTO utilizado para mandar los datos necesarios para la visualizacion de la lista de salones
 *
 * Esta clase es utilizada en peticiones HTTP para transportar
 * la información básica de un nuevo salon del sistema
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-06-03
 */
@Value
public class ResponseClassroomDTO {
    private String name;
    private String id;
    private ResponseModuleUniDTO moduleUni;
}
