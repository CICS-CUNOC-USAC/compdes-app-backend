package com.compdes.moduleUni.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para recibir los datos necesarios para la creación de un
 * modulo
 *
 * Esta clase es utilizada en peticiones HTTP para validar y transportar
 * la información básica de un nuevo modulo del sistema
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-06-05
 */
public class CreateModuleDTO {
    @NotBlank(message = "Debe ingresar el nombre del salon")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String name;
}
