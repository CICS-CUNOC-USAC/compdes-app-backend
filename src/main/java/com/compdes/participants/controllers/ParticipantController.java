package com.compdes.participants.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.compdes.participants.models.dto.request.CreateParticipantDTO;
import com.compdes.participants.services.ParticipantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 * Controlador REST para la gestión de participantes.
 * 
 * Expone endpoints relacionados con la creación de registros de participantes
 * en el sistema. Actualmente permite registrar un participante no autor.
 * 
 * Las solicitudes deben cumplir con las restricciones de validación definidas
 * en el DTO correspondiente.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-31
 */
@RestController
@RequestMapping("/api/v1/participants")
@AllArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    /**
     * Registra un nuevo participante no autor.
     *
     * @param createParticipantDTO los datos del participante a registrar
     */
    @Operation(summary = "Registrar participante no autor", responses = {
            @ApiResponse(responseCode = "201", description = "Participante creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "409", description = "Participante ya registrado con el mismo correo o documento")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNonAuthorParticipant(@RequestBody @Valid CreateParticipantDTO createParticipantDTO) {
        participantService.createNonAuthorParticipant(createParticipantDTO);
    }
}
