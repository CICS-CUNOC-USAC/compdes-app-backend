package com.compdes.common.exceptions.enums;

import com.compdes.common.exceptions.IncompleteDataException;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
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
                    ErrorCodeMessageEnum.REGISTRATION_STATUS_INCOMPLETE.getMessage()));

    private final IncompleteDataException incompleteDataException;
}
