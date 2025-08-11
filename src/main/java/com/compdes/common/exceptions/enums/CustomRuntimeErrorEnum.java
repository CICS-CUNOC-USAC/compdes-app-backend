package com.compdes.common.exceptions.enums;

import com.compdes.common.exceptions.CustomRuntimeException;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum que representa errores relacionados con fallos internos inesperados
 * durante procesos críticos del sistema.
 * 
 * Cada constante identifica una situación específica en la que no fue posible
 * completar una operación
 * debido a una condición interna que requiere atención del equipo de soporte.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-31
 */

@Getter
@AllArgsConstructor
public enum CustomRuntimeErrorEnum {

        REGISTRATION_STATUS_INCOMPLETE(
                        new CustomRuntimeException(ErrorCodeMessageEnum.REGISTRATION_STATUS_INCOMPLETE.getCode(),
                                        ErrorCodeMessageEnum.REGISTRATION_STATUS_INCOMPLETE.getMessage())),

        NO_AUTHOR_REGISTRATION_STATUS_INCOMPLETE(
                        new CustomRuntimeException(
                                        ErrorCodeMessageEnum.NO_AUTHOR_REGISTRATION_STATUS_INCOMPLETE.getCode(),
                                        ErrorCodeMessageEnum.NO_AUTHOR_REGISTRATION_STATUS_INCOMPLETE.getMessage())),
        EMAIL_REPORT_GENERATION_FAILED(
                        new CustomRuntimeException(ErrorCodeMessageEnum.EMAIL_REPORT_GENERATION_FAILED.getCode(),
                                        ErrorCodeMessageEnum.EMAIL_REPORT_GENERATION_FAILED.getMessage())),
        CSV_WRITE_FAILED(
                        new CustomRuntimeException(ErrorCodeMessageEnum.CSV_WRITE_FAILED.getCode(),
                                        ErrorCodeMessageEnum.CSV_WRITE_FAILED.getMessage()));

        private final CustomRuntimeException customRuntimeException;
}
