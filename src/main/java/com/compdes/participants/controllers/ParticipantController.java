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

import com.compdes.participants.mappers.CreateParticipantInternalDtoMapper;
import com.compdes.participants.mappers.ParticipantMapper;
import com.compdes.participants.models.dto.internal.CreateParticipantInternalDTO;
import com.compdes.participants.models.dto.request.CreateParticipantWithPaymentDTO;
import com.compdes.participants.models.dto.request.CreateParticipantByAdminDTO;
import com.compdes.participants.models.dto.response.ParticipantDTO;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.services.ParticipantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
        private final CreateParticipantInternalDtoMapper createParticipantInternalDtoMapper;

        /**
         * Obtiene la lista de todos los participantes registrados.
         *
         * Este endpoint solo puede ser accedido por usuarios con rol ADMIN.
         * Retorna la lista completa de participantes registrados en el sistema.
         *
         * @return lista de objetos ParticipantDTO que representan a los participantes
         *         registrados
         */
        @Operation(summary = "Obtener todos los participantes registrados", description = "Devuelve la lista de todos los participantes registrados. Solo accesible para usuarios con rol `ADMIN`.", security = @SecurityRequirement(name = "bearerAuth"), responses = {
                        @ApiResponse(responseCode = "200", description = "Lista de participantes obtenida exitosamente"),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado al recurso (requiere rol `ADMIN`), Token inválido o no proporcionado"),
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
         * Registra un nuevo participante.
         *
         * Permite registrar tanto participantes autores como no autores desde el
         * formulario público.
         * El participante debe adjuntar un comprobante de pago en
         * formato de imagen (campo `file`)
         * o enviar un objeto de tipo `CreatePaymentProofDTO` como parte del formulario.
         *
         * @param createParticipantWithPaymentDTO datos del participante
         * @param file                            imagen del comprobante de pago
         *                                        (opcional)
         */
        @Operation(summary = "Registrar participante", description = "Registra un nuevo participante (autor o no autor) desde el formulario público. debe proporcionar un comprobante de pago ya sea como archivo o formulario.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Formulario con los datos del participante y el archivo del comprobante (campo 'file')", required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(implementation = CreateParticipantWithPaymentDTO.class))), responses = {
                        @ApiResponse(responseCode = "201", description = "Participante creado exitosamente"),
                        @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
                        @ApiResponse(responseCode = "409", description = "Participante ya registrado con el mismo correo o documento"),
                        @ApiResponse(responseCode = "500", description = "Error al procesar el archivo o la solicitud")
        })
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public void createParticipant(
                        @ModelAttribute @Valid CreateParticipantWithPaymentDTO createParticipantWithPaymentDTO,
                        @RequestParam(name = "file", required = false) MultipartFile file) {

                // mapear el DtO externo al interno
                CreateParticipantInternalDTO internalDTO = createParticipantInternalDtoMapper
                                .createParticipantWithPaymentDtoToCreateParticipantInternalDTO(
                                                createParticipantWithPaymentDTO);
                internalDTO.setPaymentProofImageMultipartFile(file);
                participantService.createParticipant(internalDTO);
        }

        /**
         * Registra un nuevo participante desde el panel de administración.
         *
         * Este endpoint permite a un usuario administrador registrar participantes
         * autores, no autores o invitados.
         * 
         * - Si el participante es **invitado** (`isGuest = true`), no se requiere
         * comprobante de pago.
         * - Si el participante **no es invitado** (`isGuest = false`), debe
         * proporcionarse un número de comprobante válido.
         * - Los participantes **autores** tampoco requieren comprobante de pago.
         * 
         * Este endpoint está protegido y solo puede ser accedido por usuarios con el
         * rol `ADMIN`.
         *
         * @param createParticipantByAdminDTO los datos del participante a registrar
         */
        @Operation(summary = "Registrar participante (por administrador)", description = "Permite registrar un participante como autor, no autor o invitado. "
                        + "Los participantes no invitados deben proporcionar un comprobante de pago (voucherNumber). "
                        + "Solo accesible para usuarios con rol `ADMIN`.", security = @SecurityRequirement(name = "bearerAuth"), responses = {
                                        @ApiResponse(responseCode = "201", description = "Participante creado exitosamente"),
                                        @ApiResponse(responseCode = "400", description = "Datos inválidos o comprobante de pago faltante o incorrecto"),
                                        @ApiResponse(responseCode = "409", description = "Participante ya registrado con el mismo correo o documento"),
                                        @ApiResponse(responseCode = "403", description = "Acceso denegado al recurso (requiere rol `ADMIN`), Token inválido o no proporcionado"),
                                        @ApiResponse(responseCode = "500", description = "Error interno del servidor al procesar la solicitud")
                        })
        @PostMapping("/byAdmin")
        @PreAuthorize("hasRole('ADMIN')")
        @ResponseStatus(HttpStatus.CREATED)
        public void createParticipantByAdmin(
                        @RequestBody @Valid CreateParticipantByAdminDTO createParticipantByAdminDTO) {
                participantService.createParticipantByAdmin(createParticipantByAdminDTO);
        }
}
