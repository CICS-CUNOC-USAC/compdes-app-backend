package com.compdes.common.exceptions;

/**
 * Excepción lanzada cuando un recurso solicitado no existe en el sistema.
 * 
 * Este tipo de excepción es de tipo checked, y debe ser declarada o capturada
 * explícitamente. Es útil para representar errores de negocio donde una entidad
 * esperada no fue encontrada (por ejemplo, por ID).
 * 
 * Se recomienda usarla en servicios o controladores cuando no se encuentra
 * una entidad en la base de datos.

 * @author Luis Monterroso
 */
public class NotFoundException extends Exception {

    public NotFoundException( String message) {
        super(message);
    }

}
