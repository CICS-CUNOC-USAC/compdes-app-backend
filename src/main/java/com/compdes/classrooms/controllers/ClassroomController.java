package com.compdes.classrooms.controllers;

import com.compdes.classrooms.models.dto.request.AvailableClassroomsDTO;
import com.compdes.classrooms.models.dto.request.CreateClassroomDTO;
import com.compdes.classrooms.models.dto.request.EditClassroomDTO;
import com.compdes.classrooms.models.dto.response.ResponseClassroomDTO;
import com.compdes.classrooms.services.ClassroomService;
import com.compdes.common.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public void createClassroom(@RequestBody @Valid CreateClassroomDTO createClassroomDTO) throws NotFoundException {
        classroomService.createClassroom(createClassroomDTO);
    }

    /**
     * Obtienen todos los salones disponibles
     *
     */
    @Operation(summary = "Obtiene todos los salones disponibles", description = "Obtiene todos los salones registrados ")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<ResponseClassroomDTO> getAllClassrooms() {
        return classroomService.getAllClassrooms();
    }

    @Operation(summary = "Obtiene un salón por medio de su ID", responses = {
            @ApiResponse(responseCode = "200", description = "Salón encontrado exitosamente"),
    })
    @PostMapping("/available")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<ResponseClassroomDTO> getAvailableClassrooms(
            @RequestBody @Valid AvailableClassroomsDTO availableClassroomsDTO) {
        return classroomService.getAvailableClassrooms(availableClassroomsDTO);
    }

    /**
     * Edita un salon
     * */
    @Operation(summary = "Editar  un salon", responses = {
            @ApiResponse(responseCode = "200", description = "Salon editado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PatchMapping("/edit")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public void edit(@RequestBody @Valid EditClassroomDTO editClassroomDTO) throws NotFoundException {
        classroomService.editClassroom(editClassroomDTO);
    }

}
