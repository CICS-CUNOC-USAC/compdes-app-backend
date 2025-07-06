package com.compdes.reservations.controllers;

import com.compdes.common.exceptions.NotFoundException;
import com.compdes.reservations.models.dto.request.AssistanceToReservationDTO;
import com.compdes.reservations.models.dto.request.ReservationDTO;
import com.compdes.reservations.services.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public void cancelReservation(@RequestBody @Valid ReservationDTO reservationDTO) throws NotFoundException {
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

}
