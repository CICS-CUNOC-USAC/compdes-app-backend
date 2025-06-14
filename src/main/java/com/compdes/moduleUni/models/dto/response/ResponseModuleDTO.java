package com.compdes.moduleUni.models.dto.response;

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
 * @since 2025-06-05
 */
public class ResponseModuleDTO {
    @NotBlank(message = "Debe ingresar el nombre del salon")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String name;

    @NotBlank(message = "Debe de existir un id asociado al modulo")
    @Size(min = 36, max = 36, message = "identificador de salon invalido")
    private String uuid;
}
