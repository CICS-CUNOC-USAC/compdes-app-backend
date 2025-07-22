package com.compdes.classrooms.models.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

/**
 * DTO utilizado para recibir los datos necesarios para editar un salon
 *
 * Esta clase es utilizada en peticiones HTTP para validar y transportar
 * la información básica de un salon del sistema
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-07-21
 */
@Value
public class EditClassroomDTO {

    @NotBlank(message = "Debe ingresar el id del salon")
    @Size(min = 36, max = 36, message = "identificador de salon invalido")
    private String classroomId;

    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String name;

    @Size(min = 36, max = 36, message = "identificador de modulo invalido")
    private String moduleId;

    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacity;
}
