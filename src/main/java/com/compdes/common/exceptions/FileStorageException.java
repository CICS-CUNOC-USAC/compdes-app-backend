package com.compdes.common.exceptions;

/**
 * Excepción lanzada cuando ocurre un error irrecuperable durante operaciones de
 * almacenamiento de archivos,
 * como copiar, eliminar o acceder a archivos del sistema.
 * 
 * Engloba errores como conflictos de nombres, permisos insuficientes o fallos
 * del sistema de archivos.
 * 
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-02
 */
public class FileStorageException extends CustomRuntimeException {

    public FileStorageException(String code, String message) {
        super(code, message);
    }

}
