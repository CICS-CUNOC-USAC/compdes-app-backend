package com.compdes.classrooms.models.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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
public class ResponseClassroomDTO {
    @NotBlank(message = "Debe ingresar el nombre del salon")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String name;

    @NotBlank(message = "Debe de seleccionar un modulo asociado al salon")
    @Size(min = 36, max = 36, message = "identificador de salon invalido")
    private String moduleId;

    @NotBlank(message = "Debe de existir un id asociado al salon")
    @Size(min = 36, max = 36, message = "identificador de salon invalido")
    private String uuid;
}
