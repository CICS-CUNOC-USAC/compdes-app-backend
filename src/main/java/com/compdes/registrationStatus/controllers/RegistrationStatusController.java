package com.compdes.registrationStatus.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.compdes.common.exceptions.NotFoundException;
import com.compdes.registrationStatus.services.RegistrationStatusService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-05
 */
@RestController
@RequestMapping("/api/v1/registration-status")
@RequiredArgsConstructor
public class RegistrationStatusController {

    private final RegistrationStatusService registrationStatusService;

    /**
     * Aprueba el estado de registro asociado a un participante específico.
     * 
     * Este endpoint busca el estado de registro correspondiente al ID del
     * participante, verifica si ya ha sido aprobado y, si no lo ha sido, lo aprueba
     * y guarda los cambios en la base de datos.
     * 
     * @param participantId identificador del participante cuyo estado de registro
     *                      se desea aprobar
     * @throws NotFoundException     si no se encuentra un estado de registro
     *                               asociado al participante
     * @throws IllegalStateException si el estado de registro ya ha sido aprobado
     *                               previamente
     */
    @Operation(summary = "Aprobar estado de registro", description = "Aprueba el estado de registro asociado a un participante específico. "
            + "Retorna error si el estado ya ha sido aprobado o si no se encuentra. "
            + "Solo accesible para usuarios con rol `ADMIN`.", security = @SecurityRequirement(name = "bearerAuth"), responses = {
                    @ApiResponse(responseCode = "200", description = "Estado de registro aprobado exitosamente"),
                    @ApiResponse(responseCode = "404", description = "No se encontró un estado de registro asociado al participante"),
                    @ApiResponse(responseCode = "409", description = "El estado de registro ya ha sido aprobado previamente"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado al recurso (requiere rol `ADMIN`), token inválido o no proporcionado")
            })
    @PatchMapping("/approve/{participantId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public void approveRegistrationByParticipantId(@PathVariable String participantId) throws NotFoundException {
        registrationStatusService.approveRegistrationByParticipantId(participantId);
    }
}