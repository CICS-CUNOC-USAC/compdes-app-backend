package com.compdes.storedFiles.models.dto.internal;

import com.compdes.storedFiles.models.entities.StoredFile;

import lombok.Value;

/**
 * Representación interna de un archivo almacenado utilizada entre capas del
 * sistema.
 *
 * Esta clase sirve como un contenedor de datos para transportar información del
 * archivo
 * entre servicios, controladores u otros componentes.
 *
 * No debe ser utilizada para exposición externa (API) ni como entidad de
 * persistencia.
 *
 * Ejemplos de uso:
 * - Transferir datos desde un servicio de almacenamiento hacia el controlador
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-02
 */
@Value
public class StoredFileInternalDTO {

    StoredFile storedFile;
    byte[] fileByteArray;
}
