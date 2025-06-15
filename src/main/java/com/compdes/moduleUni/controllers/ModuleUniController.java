package com.compdes.moduleUni.controllers;

import com.compdes.moduleUni.models.dto.request.CreateModuleUniDTO;
import com.compdes.moduleUni.models.dto.response.ResponseModuleUniDTO;
import com.compdes.moduleUni.services.ModuleUniService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
     * @param createModuleUniDTO los datos del participante a registrar
     */
    @Operation(summary = "Registrar modulo", description = "Permite registrar un modulo en el sistema",
            security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(responseCode = "201", description = "Modulo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "409", description = "Modulo con el mismo nombre ya registrado"),
    } )
    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public void createClassroom(@RequestBody @Valid CreateModuleUniDTO createModuleUniDTO) {
        service.createModule(createModuleUniDTO);
    }

    /**
     * Obtienen todos los modulos disponibles
     *
     */
    @Operation(summary = "Obtiene todos los modulos disponibles",
            description = "Obtiene todos los modulos registrados ", responses = {
            @ApiResponse(responseCode = "201", description = "Participante creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o comprobante de pago faltante o incorrecto"),
            @ApiResponse(responseCode = "409", description = "Participante ya registrado con el mismo correo o documento"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado al recurso (requiere rol `ADMIN`), Token inválido o no proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al procesar la solicitud")
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseModuleUniDTO> getAllClassrooms(){
        return service.getAllModules();
    }
}
