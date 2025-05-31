package com.compdes.common.exceptions;

/**
 * Excepción lanzada cuando la información proporcionada por el cliente o el
 * sistema
 * es insuficiente o incompleta para ejecutar una operación correctamente.
 * 
 * Esta excepción es útil para validar datos obligatorios que no han sido
 * proporcionados,
 * como campos nulos o estructuras incompletas durante el registro o
 * procesamiento
 * de una entidad.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-31
 */
public class IncompleteDataException extends CustomRuntimeException {

    public IncompleteDataException(String code, String message) {
        super(code, message);
    }

}
