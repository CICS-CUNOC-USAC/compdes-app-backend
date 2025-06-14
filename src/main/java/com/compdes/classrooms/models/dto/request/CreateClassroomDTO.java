package com.compdes.classrooms.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para recibir los datos necesarios para la creación de un
 * salon de conferencias.
 *
 * Esta clase es utilizada en peticiones HTTP para validar y transportar
 * la información básica de un nuevo salon del sistema
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-06-03
 */
public class CreateClassroomDTO {
    @NotBlank(message = "Debe ingresar el nombre del salon")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String name;

    @NotBlank(message = "Debe de seleccionar un modulo asociado al salon")
    @Size(min = 36, max = 36, message = "identificador de salon invalido")
    private String moduleId;
}
