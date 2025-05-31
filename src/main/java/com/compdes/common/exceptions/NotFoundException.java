package com.compdes.common.exceptions;

/**
 *
 * @author Luis Monterroso
 */
public class NotFoundException extends CustomRuntimeException {

    public NotFoundException( String message) {
        super("", message);
    }

}
