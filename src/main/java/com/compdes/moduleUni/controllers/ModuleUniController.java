package com.compdes.moduleUni.controllers;

import com.compdes.moduleUni.models.dto.request.CreateModuleDTO;
import com.compdes.moduleUni.models.dto.response.ResponseModuleDTO;
import com.compdes.moduleUni.services.ModuleUniService;
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
 * @since 2025-06-05
 */
@RestController
@RequestMapping("/api/v1/modules")
@AllArgsConstructor
public class ModuleUniController {
    private final ModuleUniService service;

    /**
     * Registra un nuevo modulo  para conferencias
     *
     * @param createModuleDTO los datos del participante a registrar
     */
    @Operation(summary = "Registrar modulo", responses = {
            @ApiResponse(responseCode = "201", description = "Modulo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "409", description = "Modulo con el mismo nombre ya registrado")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createClassroom(@RequestBody @Valid CreateModuleDTO createModuleDTO) {
        service.createModule(createModuleDTO);
    }

    /**
     * Obtienen todos los salones disponibles
     *
     */
    @GetMapping
    public List<ResponseModuleDTO> getAllClassrooms(){
        return service.getAllModules();
    }
}
