package com.compdes.reservations.controllers;

import com.compdes.common.exceptions.NotFoundException;
import com.compdes.reservations.models.dto.request.AssistanceToReservationDTO;
import com.compdes.reservations.models.dto.request.ReservationDTO;
import com.compdes.reservations.models.dto.response.CountParticipantsDTO;
import com.compdes.reservations.models.dto.response.ReservationParticipantsDTO;
import com.compdes.reservations.models.dto.response.ReservationResponseDTO;
import com.compdes.reservations.models.dto.response.ValidateAssistanceDTO;
import com.compdes.reservations.services.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de reservaciones a talleres.
 *
 * Expone endpoints relacionados con acciones relacionadas a reservaciones para talleres
 *
 * Las solicitudes deben cumplir con las restricciones de validación definidas
 * en el DTO correspondiente.
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-07-01
 */
@RestController
@RequestMapping("/api/v1/reservations")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * Registra una nueva reservacion a un taller
     *
     */
    @Operation(summary = "Registrar reservacion a un taller", responses = {
            @ApiResponse(responseCode = "201", description = "Reservacion exitosa"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "409", description = "Ya se tiene un registro a una reservacion")
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PARTICIPANT')")
    public void registerReservation(@RequestBody @Valid ReservationDTO reservationDTO)
            throws NotFoundException {
        reservationService.createReservation(reservationDTO);
    }


    /**
     * Cancela una reservación al taller
     *
     */
    @Operation(summary = "Cancela una reservacion a un taller", responses = {
            @ApiResponse(responseCode = "201", description = "Cancelacion exitosa"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos")
    })
    @PostMapping("/cancel")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PARTICIPANT')")
    public void cancelReservation(@RequestBody @Valid ReservationDTO reservationDTO)
            throws NotFoundException {
        reservationService.cancelReservation(reservationDTO);
    }


    /**
     * Registra la asistencia de un participante a un taller
     * previamente reservado
     *
     */
    @Operation(summary = "Registrar reservacion a un taller", responses = {
            @ApiResponse(responseCode = "201", description = "Reservacion exitosa"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "409", description = "Ya se tiene un registro de asistencia")
    })
    @PostMapping("/registerAssistant")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public void registerAssistance(@RequestBody @Valid AssistanceToReservationDTO assistanceToReservationDTO)
            throws NotFoundException {
        reservationService.registerAssistanceToReservation(assistanceToReservationDTO);
    }

    /**
     * Obtener todas las reservaciones a un taller
     * */
    @Operation(
            summary = "Obtiene todos las reservaciones de un usuario",
            description = "Obtiene todos las reservaciones registradas para una actividad"
    )
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PARTICIPANT')")
    public List<ReservationResponseDTO> getAll() throws NotFoundException {
        return reservationService.getAll();
    }

    /**
     * Obtener las reservaciones a un taller especifico para un admin
     * */
    @Operation(
            summary = "Obtiene todos las reservaciones a un taller especifico",
            description = "Obtiene todos las reservaciones registradas para un taller"
    )
    @GetMapping("/allParticipants/{workshopId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<ReservationParticipantsDTO> getAllParticipants(@PathVariable String workshopId)
            throws NotFoundException {
        return reservationService.getAll(workshopId);
    }

    /**
     * Contar cuantos participantes estan inscritos a un taller especifico
     * */
    @Operation(
            summary = "Cuenta todos las reservaciones a un taller especifico",
            description = "Cuenta y devuelve todos las reservaciones registradas para un taller"
    )
    @GetMapping("/countParticipants/{workshopId}")
    @ResponseStatus(HttpStatus.OK)
    public CountParticipantsDTO countParticipants(@PathVariable String workshopId) throws NotFoundException {
        return new CountParticipantsDTO(reservationService.countParticipantsToWorkshop(workshopId));
    }


    /**
     * Conocer si un participante definido está asignado a un taller especifico
     * */
    @Operation(
            summary = "Cuenta todos las reservaciones a un taller especifico",
            description = "Cuenta y devuelve todos las reservaciones registradas para un taller"
    )
    @GetMapping("/isAssigned/{participantId}/{workshopId}")
    @ResponseStatus(HttpStatus.OK)
    public ValidateAssistanceDTO isAssigned(@PathVariable String participantId, @PathVariable String workshopId)
            throws NotFoundException {
        return new ValidateAssistanceDTO(reservationService.isAssigned(participantId, workshopId));
    }

}
