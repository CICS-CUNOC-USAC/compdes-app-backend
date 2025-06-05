package com.compdes.common.utils;

import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Componente encargado de extraer y construir mensajes legibles a partir
 * de errores de validación generados por el framework al recibir argumentos
 * inválidos.
 * 
 * Esta clase se utiliza para procesar excepciones del tipo
 * MethodArgumentNotValidException
 * y transformar los errores de los campos en un único mensaje de texto legible,
 * útil para mostrar en respuestas al cliente.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-31
 */
@Component
public class MethodArgumentErrorExtractor {

    /**
     * Extrae y construye un mensaje de error unificado a partir de una excepción
     * de validación de argumentos.
     * 
     * Recorre los errores de los campos individuales contenidos en la excepción y
     * los concatena en un mensaje con formato simple por línea.
     * 
     * @param exception excepción lanzada por Spring cuando los argumentos anotados
     *                  con validaciones no son válidos
     * @return un mensaje unificado con los errores de validación, uno por línea
     */
    public String extractMethodArgumentError(MethodArgumentNotValidException exception) {
        StringBuilder menssage = new StringBuilder("");
        // Recorre los errores de validación y los almacena en un mapa
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            menssage.append("- ").append(error.getDefaultMessage()).append("<br>");
        }
        return menssage.toString().trim();
    }
}
