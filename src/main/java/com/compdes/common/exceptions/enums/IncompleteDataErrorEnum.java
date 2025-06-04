package com.compdes.common.exceptions.enums;

import com.compdes.common.exceptions.IncompleteDataException;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum que encapsula errores relacionados con datos incompletos en procesos críticos del sistema.
 * 
 * Cada constante representa una condición específica en la cual la información proporcionada 
 * es insuficiente para completar correctamente una operación o flujo de negocio.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-31
 */
@Getter
@AllArgsConstructor
public enum IncompleteDataErrorEnum {

    REGISTRATION_STATUS_INCOMPLETE(
            new IncompleteDataException(ErrorCodeMessageEnum.REGISTRATION_STATUS_INCOMPLETE.getCode(),
                    ErrorCodeMessageEnum.REGISTRATION_STATUS_INCOMPLETE.getMessage())),

    NO_AUTHOR_REGISTRATION_STATUS_INCOMPLETE(
            new IncompleteDataException(ErrorCodeMessageEnum.NO_AUTHOR_REGISTRATION_STATUS_INCOMPLETE.getCode(),
                    ErrorCodeMessageEnum.NO_AUTHOR_REGISTRATION_STATUS_INCOMPLETE.getMessage()));

    private final IncompleteDataException incompleteDataException;
}
