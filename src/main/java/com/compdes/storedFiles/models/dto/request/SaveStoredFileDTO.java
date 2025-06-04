
package com.compdes.storedFiles.models.dto.request;

import java.io.InputStream;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

/**
 * DTO para guardar archivos en el sistema.
 * Contiene nombre y flujo de datos del archivo.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-02
 */
@Value
public class SaveStoredFileDTO {

    /**
     * Nombre del archivo. Este campo no incluye la extensión. Debe tener al
     * menos 3 caracteres y no puede estar vacío.
     */
    @NotBlank(message = "El nombre del archivo es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre del archivo debe tener entre 3 y 100 caracteres")
    private String originalFilename;

    /**
     * Flujo de entrada que contiene el contenido del archivo. No puede ser
     * nulo.
     */
    @NotNull(message = "El flujo del archivo no puede ser nulo")
    private InputStream fileStream;

    public SaveStoredFileDTO(
            @NotBlank(message = "El nombre del archivo es obligatorio") @Size(min = 3, max = 100, message = "El nombre del archivo debe tener entre 3 y 100 caracteres") String originalFilename,
            @NotNull(message = "El flujo del archivo no puede ser nulo") InputStream fileStream) {
        this.originalFilename = originalFilename;
        this.fileStream = fileStream;
    }

}
