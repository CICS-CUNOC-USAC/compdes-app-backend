package com.compdes.participants.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.compdes.common.exceptions.NotFoundException;
import com.compdes.common.models.dto.response.ErrorDTO;
import com.compdes.participants.mappers.CreateParticipantInternalDtoMapper;
import com.compdes.participants.mappers.ParticipantMapper;
import com.compdes.participants.models.dto.internal.CreateParticipantInternalDTO;
import com.compdes.participants.models.dto.request.CreateParticipantByAdminDTO;
import com.compdes.participants.models.dto.request.CreateParticipantWithPaymentDTO;
import com.compdes.participants.models.dto.request.ParticipantFilterDTO;
import com.compdes.participants.models.dto.request.UpdateParticipantByAdminDTO;
import com.compdes.participants.models.dto.response.AdminParticipantProfileDTO;
import com.compdes.participants.models.dto.response.ParticipantProfileDTO;
import com.compdes.participants.models.dto.response.PublicParticipantProfileDTO;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.services.ParticipantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
         * Obtiene la lista de todos los participantes registrados aplicando filtros
         * opcionales.
         * 
         * <p>
         * Este endpoint está restringido a usuarios con rol ADMIN y expone datos
         * sensibles
         * de los participantes, como correo electrónico, número de documento y estado
         * de aprobación.
         * Se debe utilizar con precaución y únicamente con fines administrativos.
         * </p>
         * 
         * @param filters objeto que contiene los criterios de búsqueda opcionales
         * @return lista de objetos AdminParticipantProfileDTO que representan a los
         *         participantes encontrados
         */
        @Operation(summary = "Obtener todos los participantes registrados", description = """
                        Devuelve la lista de todos los participantes registrados, permitiendo aplicar filtros opcionales como nombre, correo, organización, estado de aprobación y tipo de pago.
                        Este endpoint está restringido a usuarios con rol `ADMIN` y expone información sensible del sistema.
                        """, security = @SecurityRequirement(name = "bearerAuth"), responses = {
                        @ApiResponse(responseCode = "200", description = "Lista de participantes obtenida exitosamente"),
                        @ApiResponse(responseCode = "403", description = "Acceso denegado al recurso (requiere rol `ADMIN`), Token inválido o no proporcionado", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor al recuperar los participantes", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
        })
        @GetMapping("/all")
        @PreAuthorize("hasRole('ADMIN')")
        @ResponseStatus(HttpStatus.OK)
        public Page<AdminParticipantProfileDTO> getAllParticipants(
                        @Parameter(description = "TODOS OPCIONALES, MANDARLO TAMBIEN ES OPCIONAL") @ModelAttribute ParticipantFilterDTO filters,
                        Pageable pageable) {

                Page<Participant> participants = participantService.getAllParticipants(filters, pageable);
                return participants.map(participantMapper::participantToPrivateParticipantInfoDto);
        }

        /**
         * Obtiene la información privada de un participante a partir de su ID.
         * 
         * Este endpoint solo puede ser accedido por usuarios con rol ADMIN. Retorna
         * los detalles completos del participante, incluyendo correo, teléfono,
         * documento de identificación y estado de registro.
         * 
         * @param id identificador único del participante
         * @return DTO con la información privada del participante
         * @throws NotFoundException si no se encuentra ningún participante con el ID
         *                           proporcionado
         */
        @Operation(summary = "Obtener perfil de un participante por ID (uso administrativo)", description = "Retorna los datos completos de un participante a partir de su ID. "
                        + "Incluye información privada. "
                        + "Solo accesible para usuarios con rol `ADMIN`.", security = @SecurityRequirement(name = "bearerAuth"), responses = {
                                        @ApiResponse(responseCode = "200", description = "Participante encontrado exitosamente"),
                                        @ApiResponse(responseCode = "404", description = "No se encontró un participante con el ID especificado", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                                        @ApiResponse(responseCode = "403", description = "Acceso denegado al recurso (requiere rol `ADMIN`), token inválido o no proporcionado", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                                        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
                        })
        @GetMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        @ResponseStatus(HttpStatus.OK)
        public AdminParticipantProfileDTO getAdminParticipantProfile(
                        @PathVariable String id) throws NotFoundException {
                Participant participant = participantService
                                .getParticipantById(id);
                AdminParticipantProfileDTO participantInfoDTO = participantMapper
                                .participantToPrivateParticipantInfoDto(participant);
                return participantInfoDTO;
        }

        /**
         * Recupera el estado público de la inscripción de un participante mediante su
         * documento de identificación.
         * 
         * Este endpoint no requiere autenticación y permite consultar el estado de
         * registro (público) de una persona
         * que se haya inscrito utilizando su número de documento. La respuesta no
         * incluye datos sensibles del participante.
         * 
         * @param identificationDocument documento de identificación del participante
         * @return información pública de la inscripción correspondiente
         * @throws NotFoundException si no se encuentra una inscripción asociada al
         *                           documento proporcionado
         */
        @Operation(summary = "Consultar inscripción pública por documento de identificación", description = "Permite consultar el estado público de una inscripción mediante el documento de identificación. "
                        + "No requiere autenticación y no expone información sensible del participante.", responses = {
                                        @ApiResponse(responseCode = "200", description = "Inscripción encontrada exitosamente"),
                                        @ApiResponse(responseCode = "404", description = "No se encontró ninguna inscripción asociada al documento ingresado", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                                        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
                        })

        @GetMapping("/public-inscription/by-document/{identificationDocument}")
        @ResponseStatus(HttpStatus.OK)
        public PublicParticipantProfileDTO getParticipantByIdentificationDocument(
                        @PathVariable String identificationDocument) throws NotFoundException {

                Participant participant = participantService
                                .getParticipantByIdentificationDocument(identificationDocument);
                PublicParticipantProfileDTO participantInfoDTO = participantMapper
                                .participantToPublicParticipantInfoDto(participant);
                return participantInfoDTO;
        }

        /**
         * Obtiene el perfil del participante autenticado.
         * 
         * <p>
         * Este método recupera la información detallada del perfil del usuario actual
         * utilizando su nombre de usuario desde el contexto de autenticación.
         * </p>
         * 
         * @param userDetails detalles del usuario autenticado extraídos del token JWT
         * @return DTO con la información del perfil del participante autenticado
         * @throws NotFoundException si no se encuentra un participante vinculado al
         *                           usuario
         */
        @Operation(summary = "Obtener perfil propio", description = "Retorna el perfil del participante autenticado basado en el token Bearer JWT proporcionado.", security = @SecurityRequirement(name = "bearerAuth"), responses = {
                        @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente"),
                        @ApiResponse(responseCode = "401", description = "Token de autenticación inválido o ausente", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                        @ApiResponse(responseCode = "404", description = "No se encontró un participante vinculado al usuario autenticado", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
        })
        @GetMapping("/my-profile")
        @PreAuthorize("hasRole('PARTICIPANT')")
        @ResponseStatus(HttpStatus.OK)
        public ParticipantProfileDTO getMyProfile(@AuthenticationPrincipal UserDetails userDetails)
                        throws NotFoundException {
                Participant participant = participantService
                                .getParticipantByUserName(userDetails.getUsername());
                ParticipantProfileDTO participantInfoDTO = participantMapper
                                .participantToParticipantProfileDto(participant);
                return participantInfoDTO;
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
                        @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                        @ApiResponse(responseCode = "409", description = "Participante ya registrado con el mismo correo o documento", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Error al procesar el archivo o la solicitud", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
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
                                        @ApiResponse(responseCode = "400", description = "Datos inválidos o comprobante de pago faltante o incorrecto", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                                        @ApiResponse(responseCode = "409", description = "Participante ya registrado con el mismo correo o documento", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                                        @ApiResponse(responseCode = "403", description = "Acceso denegado al recurso (requiere rol `ADMIN`), Token inválido o no proporcionado", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                                        @ApiResponse(responseCode = "500", description = "Error interno del servidor al procesar la solicitud", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
                        })
        @PostMapping("/byAdmin")
        @PreAuthorize("hasRole('ADMIN')")
        @ResponseStatus(HttpStatus.CREATED)
        public void createParticipantByAdmin(
                        @RequestBody @Valid CreateParticipantByAdminDTO createParticipantByAdminDTO) {
                participantService.createParticipantByAdmin(createParticipantByAdminDTO);
        }

        @Operation(summary = "Actualizar participante", description = "Actualiza los datos de un participante desde el panel de administración. "
                        +
                        "Solo puede enviarse uno de los siguientes campos opcionales: 'voucherNumber' o 'paymentProof'. "
                        +
                        "Si se envía 'voucherNumber', el participante debe haber realizado un pago en efectivo. " +
                        "Si se envía 'paymentProof.link', el participante debe haber pagado con tarjeta. " +
                        "También se valida que el correo electrónico y el documento de identificación no estén ya registrados por otro participante. "
                        +
                        "Requiere rol `ADMIN`.", security = @SecurityRequirement(name = "bearerAuth"), responses = {
                                        @ApiResponse(responseCode = "200", description = "Participante actualizado correctamente"),
                                        @ApiResponse(responseCode = "400", description = "Datos inválidos o reglas de negocio incumplidas. Puede ocurrir si: se intenta guardar un link sin pago con tarjeta, un número de comprobante sin pago en efectivo, o si se envían ambos campos opcionales a la vez.", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                                        @ApiResponse(responseCode = "404", description = "No se encontró un participante con el ID proporcionado", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                                        @ApiResponse(responseCode = "409", description = "Ya existe otro participante con el mismo correo electrónico o documento de identificación", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                                        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
                        })
        @PatchMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        @ResponseStatus(HttpStatus.OK)
        public void updateParticipantByAdmin(
                        @PathVariable String id, @RequestBody @Valid UpdateParticipantByAdminDTO dto)
                        throws NotFoundException {
                participantService.updateParticipantByAdmin(id, dto);
        }

        @Operation(summary = "Eliminar participante", description = "Elimina un participante si aún no ha sido confirmado. "
                        + "En caso de que el participante ya esté confirmado, se rechaza la operación. Accesible para `ADMIN`", security = @SecurityRequirement(name = "bearerAuth"), responses = {
                                        @ApiResponse(responseCode = "204", description = "Participante eliminado exitosamente"),
                                        @ApiResponse(responseCode = "404", description = "Participante no encontrado", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
                                        @ApiResponse(responseCode = "409", description = "El participante ya fue confirmado y no puede ser eliminado", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
                        })
        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        @ResponseStatus(HttpStatus.OK)
        public void deleteParticipant(@PathVariable String id) throws NotFoundException {
                participantService.deleteParticipant(id);
        }
}
