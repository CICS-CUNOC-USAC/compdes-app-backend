package com.compdes.common.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-27
 */
@Getter
@Setter
public class CustomRuntimeException extends RuntimeException {

    private String code;

    public CustomRuntimeException(String code, String message) {
        super(message);
        this.code = code;
    }
}