package com.compdes.storedFiles.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.compdes.common.exceptions.NotFoundException;
import com.compdes.storedFiles.models.dto.internal.StoredFileInternalDTO;
import com.compdes.storedFiles.services.StoredFileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(StoredFileController.BASE_PATH)
@RequiredArgsConstructor
public class StoredFileController {

    public static final String BASE_PATH = "/api/v1/stored-files";
    public static final String BASE_GET_FILE_BY_ID = "/download/";
    private static final String GET_FILE_BY_ID = BASE_GET_FILE_BY_ID + "{id}";

    private final StoredFileService storedFileService;

    /**
     * Obtiene un archivo almacenado a partir de su
     * ID.
     * 
     * @param id identificador del archivo almacenado
     * @return respuesta HTTP con el contenido del archivo en forma de arreglo de
     *         bytes y sus encabezados
     * @throws NotFoundException si no se encuentra un archivo con el ID
     *                           especificado
     */
    @Operation(summary = "Obtener archivo almacenado por ID", description = "Retorna el contenido binario de un archivo almacenado, como una imagen, según su ID. Exclusivo para usuarios con rol `ADMIN`.", security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(responseCode = "200", description = "Archivo recuperado exitosamente", content = @Content(mediaType = "application/octet-stream", schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "404", description = "Archivo no encontrado con el ID proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al recuperar el archivo")
    })
    @GetMapping(GET_FILE_BY_ID)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> getQrImageById(@PathVariable String id) throws NotFoundException {
        StoredFileInternalDTO dto = storedFileService.getStoredFileById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(dto.getStoredFile().getMimeType()));
        return new ResponseEntity<>(dto.getFileByteArray(), headers, HttpStatus.OK);
    }
}
