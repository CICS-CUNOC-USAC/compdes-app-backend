package com.compdes.classrooms.controllers;

import com.compdes.classrooms.models.dto.request.CreateClassroomDTO;
import com.compdes.classrooms.models.dto.response.ResponseClassroomDTO;
import com.compdes.classrooms.services.ClassroomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de salones.
 *
 * Expone endpoints relacionados con la creación gestion administrativa
 * de salones en la universidad
 *
 * Las solicitudes deben cumplir con las restricciones de validación definidas
 * en el DTO correspondiente.
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-06-01
 */
@RestController
@RequestMapping("/api/v1/classrooms")
@AllArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;

    /**
     * Registra un nuevo salon de clases para conferencias
     *
     * @param createClassroomDTO los datos del participante a registrar
     */
    @Operation(summary = "Registrar salon", responses = {
            @ApiResponse(responseCode = "201", description = "Salon creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "409", description = "Salón con el mismo nombre y facultad ya registrado")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createClassroom(@RequestBody @Valid CreateClassroomDTO createClassroomDTO) {
        classroomService.createClassroom(createClassroomDTO);
    }

    /**
     * Obtienen todos los salones disponibles
     *
     */
    @GetMapping
    public List<ResponseClassroomDTO> getAllClassrooms(){
        return classroomService.getAllClassrooms();
    }
}
