package com.compdes.common.exceptions;

/**
 * Excepción utilizada para indicar que un recurso que se intenta crear
 * ya existe en el sistema.
 * 
 * Esta excepción es apropiada para manejar casos de duplicidad de datos,
 * como correos electrónicos, documentos de identificación u otros campos
 * únicos.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-30
 */
public class DuplicateResourceException extends CustomRuntimeException {

    public DuplicateResourceException(String message) {
        super("", message);
    }
}
