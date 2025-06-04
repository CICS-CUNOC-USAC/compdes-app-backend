package com.compdes.participants.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.compdes.participants.mappers.CreateNonAuthorParticipantInternalDtoMapper;
import com.compdes.participants.mappers.ParticipantMapper;
import com.compdes.participants.models.dto.internal.CreateNonAuthorParticipantInternalDTO;
import com.compdes.participants.models.dto.request.CreateAuthorParticipantDTO;
import com.compdes.participants.models.dto.request.CreateNonAuthorParticipantDTO;
import com.compdes.participants.models.dto.request.CreateParticipantByAdminDTO;
import com.compdes.participants.models.dto.response.ParticipantDTO;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.services.ParticipantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
        private final ParticipantMapper participantMapper;
        private final CreateNonAuthorParticipantInternalDtoMapper createNonAuthorParticipantInternalDtoMapper;

        /**
         * Obtiene la lista de todos los participantes registrados.
         *
         * @return lista de objetos ParticipantDTO que representan a los participantes
         *         registrados
         */
        @Operation(summary = "Obtener todos los participantes registrados", responses = {
                        @ApiResponse(responseCode = "200", description = "Lista de participantes obtenida exitosamente"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor al recuperar los participantes")
        })
        @GetMapping("/all")
        @PreAuthorize("hasRole('ADMIN')")
        @ResponseStatus(HttpStatus.OK)
        public List<ParticipantDTO> getAllParticipants() {

                List<Participant> participants = participantService.getAllParticipants();
                List<ParticipantDTO> participantDTOs = participantMapper.participantsToParticipantDtos(participants);
                return participantDTOs;
        }

        /**
         * Registra un nuevo participante no autor.
         *
         * @param createParticipantDTO los datos del participante a registrar
         */
        @Operation(summary = "Registrar participante no autor", description = "Registra un nuevo participante no autor enviando datos y archivo de comprobante de pago en un formulario multipart.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Formulario con los datos del participante y el archivo (llamado \"file\")", required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(implementation = CreateNonAuthorParticipantDTO.class))), responses = {
                        @ApiResponse(responseCode = "201", description = "Participante creado exitosamente"),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
                        @ApiResponse(responseCode = "409", description = "Participante ya registrado con el mismo correo o documento")
        })
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public void createNonAuthorParticipant(
                        @ModelAttribute @Valid CreateNonAuthorParticipantDTO createParticipantDTO,
                        @RequestParam(name = "file", required = false) MultipartFile file) {

                // mapear el DtO externo al interno
                CreateNonAuthorParticipantInternalDTO authorParticipantInternalDTO = createNonAuthorParticipantInternalDtoMapper
                                .createNonAuthorParticipantDtoToCreateNonAuthorParticipantInternalDTO(
                                                createParticipantDTO);
                authorParticipantInternalDTO.setPaymentProofImage(file);
                participantService.createNonAuthorParticipant(authorParticipantInternalDTO);
        }

        /**
         * Registra un nuevo participante (autor o no autor) desde el panel de
         * administración.
         *
         * Si el participante no es autor, se debe incluir un número de comprobante
         * válido.
         * Los participantes autores no requieren comprobante de pago.
         * 
         * @param createParticipantByAdminDTO
         */
        @Operation(summary = "Registrar participante (por administrador)", description = "Permite registrar un participante como autor o no autor. Si el participante no es autor, debe proporcionar un comprobante de pago. Solo accesible para usuarios con rol ADMIN.", responses = {
                        @ApiResponse(responseCode = "201", description = "Participante creado exitosamente"),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos, faltantes o comprobante de pago no válido"),
                        @ApiResponse(responseCode = "409", description = "Participante ya registrado con el mismo correo o documento"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor al procesar la solicitud o guardar archivos")
        })
        @PostMapping("/byAdmin")
        @PreAuthorize("hasRole('ADMIN')")
        @ResponseStatus(HttpStatus.CREATED)
        public void createParticipantByAdmin(
                        @RequestBody @Valid CreateParticipantByAdminDTO createParticipantByAdminDTO) {
                participantService.createParticipantByAdmin(createParticipantByAdminDTO);
        }

        /**
         * Registra un nuevo participante autor.
         *
         * @param createParticipantDTO los datos del participante autor a registrar
         */
        @Operation(summary = "Registrar participante autor", responses = {
                        @ApiResponse(responseCode = "201", description = "Participante autor creado exitosamente"),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
                        @ApiResponse(responseCode = "409", description = "Participante ya registrado con el mismo correo o documento")
        })
        @PostMapping("/author")
        @ResponseStatus(HttpStatus.CREATED)
        public void createAuthorParticipant(@RequestBody @Valid CreateAuthorParticipantDTO createParticipantDTO) {
                participantService.createAuthorParticipant(createParticipantDTO);
        }
}
