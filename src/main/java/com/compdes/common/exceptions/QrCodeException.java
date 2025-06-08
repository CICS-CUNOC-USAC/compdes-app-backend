package com.compdes.common.exceptions;

/**
 * Excepción personalizada utilizada para representar errores relacionados con
 * la gestión de códigos QR dentro del sistema.
 * 
 * Esta excepción permite incluir un código de error y un mensaje detallado,
 * facilitando el manejo controlado de fallos específicos como la falta de QR
 * disponibles.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-06
 */
public class QrCodeException extends CustomRuntimeException {

    public QrCodeException(String code, String message) {
        super(code, message);
    }
}
