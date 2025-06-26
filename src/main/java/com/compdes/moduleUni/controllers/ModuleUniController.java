package com.compdes.moduleUni.controllers;

import com.compdes.moduleUni.mappers.ModuleUniMapper;
import com.compdes.moduleUni.models.dto.request.CreateModuleUniDTO;
import com.compdes.moduleUni.models.dto.response.BasicResponseModuleUniDTO;
import com.compdes.moduleUni.models.dto.response.ResponseModuleUniDTO;
import com.compdes.moduleUni.models.entities.ModuleUni;
import com.compdes.moduleUni.services.ModuleUniService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequiredArgsConstructor
public class ModuleUniController {
    private final ModuleUniService service;
    private final ModuleUniMapper moduleUniMapper;

    /**
     * Registra un nuevo modulo para conferencias
     *
     * @param createModuleUniDTO los datos del modulo a registrar
     */
    @Operation(summary = "Registrar modulo", description = "Permite registrar un modulo en el sistema", security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(responseCode = "201", description = "Modulo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "409", description = "Modulo con el mismo nombre ya registrado"),
    })
    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public void createClassroom(@RequestBody @Valid CreateModuleUniDTO createModuleUniDTO) {
        service.createModule(createModuleUniDTO);
    }

    /**
     * Obtiene los módulos registrados en formato paginado.
     * 
     * Este método está destinado exclusivamente para usuarios con rol de
     * administrador.
     * 
     * @param pageable objeto que contiene la información de paginación (número de
     *                 página, tamaño, ordenamiento)
     * @return una página con los módulos registrados
     */

    @Operation(summary = "Retorna los módulos registrados en formato paginado. Para `ADMIN`.", security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(responseCode = "200", description = "Lista paginada de módulos obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "Autenticación fallida: Token inválido o ausente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: No tiene permisos de administrador"),
            @ApiResponse(responseCode = "500", description = "Ocurrió un error inesperado en el servidor")
    })
    @GetMapping("/paginated-modules")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public Page<ResponseModuleUniDTO> getPaginatedModules(Pageable pageable) {
        Page<ModuleUni> pageModuleUni = service.getPaginatedModules(pageable);
        return pageModuleUni.map(moduleUniMapper::moduleToResponseDto);
    }

    /**
     * Obtienen todos los modulos disponibles
     *
     */
    @Operation(summary = "Obtiene todos los módulos disponibles, vista para `ADMIN`", security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(responseCode = "200", description = "Lista de módulos obtenida correctamente"),
            @ApiResponse(responseCode = "401", description = "Autenticación fallida: Token inválido o ausente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: No tiene permisos de administrador"),
            @ApiResponse(responseCode = "500", description = "Ocurrió un error inesperado en el servidor")
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<ResponseModuleUniDTO> getAllModules() {
        List<ModuleUni> modules = service.getAllModules();
        return moduleUniMapper.moduleToResponseDto(modules);
    }

    /**
     * Obtienen todos los modulos disponibles
     * Sin informacion sensible
     *
     */
    @Operation(summary = "Obtiene todos los modulos disponibles", description = "Obtiene todos los modulos registrados ")
    @GetMapping("/consultAll")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PARTICIPANT')")
    public List<BasicResponseModuleUniDTO> consultAll() {
        List<ModuleUni> modules = service.getAllModules();
        return moduleUniMapper.moduleToBasicResponseDto(modules);
    }

}
