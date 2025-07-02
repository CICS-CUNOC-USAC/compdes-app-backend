package com.compdes.reservations.controllers;

import com.compdes.classrooms.services.ClassroomService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
