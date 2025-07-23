package com.compdes.auth.password.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.compdes.auth.password.models.dto.request.ChangePasswordDTO;
import com.compdes.auth.password.services.PasswordService;
import com.compdes.common.exceptions.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v2/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    /**
     * Cambia la contraseña del usuario asociado a un participante.
     * 
     * @param newPassword   objeto que contiene la nueva contraseña a establecer
     * @param participantId ID del participante cuyo usuario será actualizado
     * @throws NotFoundException si el participante no existe
     */
    @Operation(summary = "Cambiar la contraseña de un participante", description = """
            Actualiza la contraseña del usuario asociado a un participante registrado.
            Este endpoint está restringido a usuarios con rol `ADMIN` y requiere que el participante tenga una cuenta de usuario válida.
            """, security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(responseCode = "200", description = "Contraseña actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "El participante no tiene un usuario asociado o la contraseña no cumple los requisitos"),
            @ApiResponse(responseCode = "404", description = "Participante no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar actualizar la contraseña")
    })
    @PatchMapping("/{participantId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void changePasswordUser(
            @RequestBody @Valid ChangePasswordDTO newPassword, @PathVariable String participantId)
            throws NotFoundException {
        passwordService.changePasswordUser(newPassword, participantId);
    }

}
