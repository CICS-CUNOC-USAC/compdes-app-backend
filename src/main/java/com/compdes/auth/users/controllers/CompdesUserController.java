package com.compdes.auth.users.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.compdes.auth.users.mappers.CompdesUserMapper;
import com.compdes.auth.users.models.dto.request.CreateNonParticipantCompdesUserDTO;
import com.compdes.auth.users.models.dto.request.CreateParticipanCompdestUserDTO;
import com.compdes.auth.users.models.dto.response.CompdesUserDTO;
import com.compdes.auth.users.models.entities.CompdesUser;
import com.compdes.auth.users.services.CompdesUserService;
import com.compdes.common.exceptions.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-01
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class CompdesUserController {

    private final CompdesUserMapper compdesUserMapper;
    private final CompdesUserService compdesUserService;

    /**
     * Crea un nuevo usuario del sistema. Solo accesible por usuarios con rol ADMIN.
     *
     * @param createCompdesUserDTO datos del usuario a registrar
     * @return datos básicos del usuario creado
     * @throws NotFoundException si no se encuentra ningún rol con la
     *                           etiqueta
     *                           proporcionada
     */
    @Operation(summary = "Crear nuevo usuario", description = "Crea un nuevo usuario del sistema (no participante). Solo accesible para usuarios con rol `ADMIN`.", security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Si el rol `Participante` intenta asiganrse,"),
            @ApiResponse(responseCode = "409", description = "Nombre de usuario ya registrado"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado al recurso (requiere rol `ADMIN`), Token inválido o no proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al procesar la solicitud")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public CompdesUserDTO createNonParticipantUser(
            @RequestBody @Valid CreateNonParticipantCompdesUserDTO createCompdesUserDTO)
            throws NotFoundException {
        CompdesUser compdesUser = compdesUserService.createNonParticipantUser(createCompdesUserDTO);
        return compdesUserMapper.compdesUserToCompdesUserDTO(compdesUser);
    }

    @Operation(summary = "Finalizar creación de cuenta de participante", description = """
            Completa el registro de un usuario asociado a un participante previamente aprobado.

            Aunque el cuerpo de la solicitud incluye un nombre de usuario, este valor será ignorado:
            el sistema utilizará automáticamente el correo electrónico registrado del participante
            como su nombre de usuario.

            Es necesario proporcionar la contraseña deseada y el documento de identificación previamente utilizado
            durante la inscripción para validar la operación.
            """, responses = {
            @ApiResponse(responseCode = "201", description = "Cuenta de participante creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o el documento de identificación no coincide"),
            @ApiResponse(responseCode = "404", description = "No se encontró el usuario o participante asociado"),
            @ApiResponse(responseCode = "409", description = "El correo electrónico ya está en uso como nombre de usuario")
    })
    @PatchMapping("/finalize/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompdesUserDTO finalizeParticipantAccountCreation(
            @PathVariable String userId,
            @RequestBody @Valid CreateParticipanCompdestUserDTO createCompdesUserDTO)
            throws NotFoundException {
        CompdesUser compdesUser = compdesUserService.finalizeParticipantAccountCreation(userId, createCompdesUserDTO);
        return compdesUserMapper.compdesUserToCompdesUserDTO(compdesUser);
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param userId ID único del usuario a buscar
     * @return DTO del usuario encontrado
     * @throws NotFoundException si no se encuentra un usuario con el ID dado
     */
    @Operation(summary = "Buscar usuario por ID", description = "Busca un usuario específico por su ID. Solo accesible para usuarios con rol `ADMIN`.", security = @SecurityRequirement(name = "bearerAuth"), responses = {

            @ApiResponse(responseCode = "200", description = "Usuario encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado al recurso (requiere rol `ADMIN`), Token inválido o no proporcionado"),

    })
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public CompdesUserDTO findUserById(@PathVariable String userId) throws NotFoundException {
        CompdesUser compdesUser = compdesUserService.findUserById(userId);
        return compdesUserMapper.compdesUserToCompdesUserDTO(compdesUser);
    }

    /**
     * Obtiene un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario a buscar
     * @return DTO del usuario encontrado
     * @throws NotFoundException si no se encuentra un usuario con el nombre
     *                           proporcionado
     */
    @Operation(summary = "Buscar usuario por nombre de usuario", description = "Busca un usuario específico por su nombre de usuario. Solo accesible para usuarios con rol `ADMIN`.", security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado correctamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado al recurso (requiere rol `ADMIN`), Token inválido o no proporcionado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el nombre indicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor"),
    })
    @GetMapping("/byUsername/{username}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public CompdesUserDTO createNonAuthorParticipant(@PathVariable String username) throws NotFoundException {
        CompdesUser compdesUser = compdesUserService.findUserByUsername(username);
        return compdesUserMapper.compdesUserToCompdesUserDTO(compdesUser);
    }

    /**
     * Devuelve la información del usuario actualmente autenticado.
     *
     * @param userDetails información del usuario extraída del contexto de seguridad
     * @return DTO con información básica del usuario autenticado
     * @throws NotFoundException
     */
    @Operation(summary = "Obtener usuario autenticado", description = "Devuelve la información del usuario autenticado basado en el token JWT proporcionado.", security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(responseCode = "200", description = "Usuario autenticado encontrado"),
            @ApiResponse(responseCode = "403", description = "Token inválido o no proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/me")
    public CompdesUserDTO getAuthenticatedUser(@AuthenticationPrincipal UserDetails userDetails)
            throws NotFoundException {
        CompdesUser compdesUser = compdesUserService.findUserByUsername(userDetails.getUsername());
        return compdesUserMapper.compdesUserToCompdesUserDTO(compdesUser);
    }
}