package com.compdes.common.exceptions;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-26
 */
public class InvalidTokenException extends CustomRuntimeException {

    public InvalidTokenException(String code, String message) {
        super(code, message);
    }

}