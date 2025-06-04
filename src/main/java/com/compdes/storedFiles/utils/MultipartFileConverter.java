package com.compdes.storedFiles.utils;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.compdes.common.exceptions.FileStorageException;
import com.compdes.common.exceptions.enums.FileStorageErrorEnum;
import com.compdes.storedFiles.models.dto.request.SaveStoredFileDTO;

/**
 * Utilidad para convertir archivos {@link MultipartFile} en objetos
 * {@link SaveStoredFileDTO} utilizados en la lógica de persistencia de
 * archivos.
 * 
 * Centraliza la validación y transformación de archivos recibidos en
 * solicitudes
 * HTTP multipart/form-data.
 * 
 * Ideal para delegar esta responsabilidad fuera de los controladores y
 * mantenerlos livianos.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-03
 */

@Component
public class MultipartFileConverter {

    /**
     * Convierte un archivo {@link MultipartFile} recibido en una solicitud HTTP
     * en una instancia de {@link SaveStoredFileDTO}, que encapsula el nombre
     * original
     * del archivo y su flujo de datos para su posterior persistencia.
     * 
     * Este método valida que el archivo no sea nulo ni esté vacío antes de
     * procesarlo.
     * En caso contrario, lanza una {@link IllegalArgumentException}.
     * 
     * Si ocurre un error al obtener el flujo de entrada del archivo, se lanza una
     * excepción personalizada encapsulada en
     * {@link FileStorageErrorEnum#FILE_INPUT_STREAM_ERROR}.
     * 
     * @param multipartFile archivo enviado como parte de un formulario multipart
     * @return un objeto {@link SaveStoredFileDTO} que contiene los metadatos y
     *         contenido del archivo
     * @throws IllegalArgumentException si el archivo es nulo o está vacío
     * @throws FileStorageException     si ocurre un error al acceder al contenido
     *                                  del archivo
     */
    public SaveStoredFileDTO convertMultipartFileToSaveStoredFileDTO(MultipartFile multipartFile) {

        // Validación del archivo de comprobante de pago
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException(
                    "No se recibió ningún archivo de comprobante de pago o el archivo está vacío. Verifica que se haya adjuntado correctamente.");
        }

        try {
            InputStream fileStream = multipartFile.getInputStream();
            String originalFileName = multipartFile.getOriginalFilename();
            SaveStoredFileDTO saveStoredFileDTO = new SaveStoredFileDTO(originalFileName,
                    fileStream);
            return saveStoredFileDTO;
        } catch (IOException e) {
            throw FileStorageErrorEnum.FILE_INPUT_STREAM_ERROR.getFileStorageException();
        }

    }
}
