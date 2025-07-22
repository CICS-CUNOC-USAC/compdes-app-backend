package com.compdes.common.exceptions.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum que representa errores relacionados reservaciones a talleres
 *
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-07-07
 */
@Getter
@AllArgsConstructor
public enum ReservationErrorsEnum {
    INVALID_DATE_RANGE(new IllegalArgumentException(
            "Los rangos de fecha son invalidos"
    )),
    NO_WORKSHOP_EXCEPTION(new IllegalArgumentException(
            "No se puede reservar cupo a una actividad que no sea un taller"
    )),
    NO_SPACE_EXCEPTION(new IllegalArgumentException(
            "El salon donde se impartira el taller no tiene espacio, no se puede asignar"
    )),
    INVALID_SCHEDULE_EXCEPTION(new IllegalArgumentException(
            "Ya se ha reservado un taller a en este rango de tiempo"
    )),
    CANNOT_ASSIG(new IllegalArgumentException(
            "Horario invalido para registrar la asistencia al taller, "
    )),
    CANNOT_CANCEL(new IllegalArgumentException(
            "El taller ya se impartió o se está impartiendo, ya no se puede cancelar la reservacion"
    ))
    ;
    private final IllegalArgumentException exception;
}
