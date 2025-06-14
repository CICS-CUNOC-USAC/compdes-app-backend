package com.compdes.qrCodes.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.compdes.common.exceptions.NotFoundException;
import com.compdes.qrCodes.services.QrCodeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

/**
 * Controlador encargado de la gestión de códigos QR asociados a los
 * participantes.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-13
 */
@RestController
@RequestMapping(QrCodeController.BASE_PATH)
@RequiredArgsConstructor
public class QrCodeController {

    private final QrCodeService qrCodeService;

    /**
     * Ruta base del controlador de códigos QR.
     * 
     * Define el prefijo común para todos los endpoints relacionados con códigos QR.
     * Se utiliza en la anotación {@code @RequestMapping} de la clase para agrupar
     * lógicamente los recursos bajo la ruta {@code /api/v1/qrs}.
     */
    public static final String BASE_PATH = "/api/v1/qrs";

    /**
     * Prefijo común para los endpoints que exponen imágenes de códigos QR.
     * 
     * Se utiliza como base para construir rutas específicas que permiten acceder
     * a una imagen de QR por ID.
     */
    private static final String BASE_GET_QR_IMAGE_BY_ID = "/download";
    public static final String BASE_GET_QR_IMAGE_BY_ID_FOR_PARTICIPANT = BASE_GET_QR_IMAGE_BY_ID + "/my-code";
    public static final String BASE_GET_QR_IMAGE_BY_ID_FOR_ADMIN = BASE_GET_QR_IMAGE_BY_ID + "/admin";

    /**
     * Ruta completa para acceder a la imagen del código QR mediante su ID.
     * 
     * Construida a partir del prefijo {@link #BASE_GET_QR_IMAGE_BY_ID} seguido de
     * un identificador dinámico, representa la ruta relativa utilizada en la
     * anotación
     * {@code @GetMapping} del método que sirve la imagen QR.
     */
    private static final String GET_QR_IMAGE_BY_ID_FOR_ADMIN = BASE_GET_QR_IMAGE_BY_ID_FOR_ADMIN + "/{id}";
    private static final String GET_QR_IMAGE_BY_ID_FOR_PARTICIPANT = BASE_GET_QR_IMAGE_BY_ID_FOR_PARTICIPANT;

    @Operation(summary = "Obtener imagen del código QR por ID", description = "Devuelve la imagen PNG del código QR correspondiente al ID proporcionado. El código QR debe estar vinculado a un participante. Protegido con `bearerAuth`, accesible para usuarios rol `ADMIN`, `PARTICIPANT`.", security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(responseCode = "200", description = "Imagen del código QR generada correctamente", content = @Content(mediaType = "image/png")),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún código QR con el ID proporcionado"),
            @ApiResponse(responseCode = "409", description = "El código QR no está vinculado a ningún participante"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al generar la imagen")
    })
    @GetMapping(GET_QR_IMAGE_BY_ID_FOR_ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> getQrImageByIdForAdmin(@PathVariable String id) throws NotFoundException {
        byte[] qrImage = qrCodeService.getQrImageByQrId(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
    }

    @Operation(summary = "Obtener código QR del participante autenticado", description = "Devuelve la imagen PNG del código QR asociado al usuario autenticado, disponible para rol `PARTICIPANT`."
            + " Solo disponible para participantes que ya tengan un QR asignado.", security = @SecurityRequirement(name = "bearerAuth"), responses = {
                    @ApiResponse(responseCode = "200", description = "Imagen del código QR obtenida correctamente", content = @Content(mediaType = "image/png")),
                    @ApiResponse(responseCode = "404", description = "No se encontró un código QR vinculado al usuario"),
                    @ApiResponse(responseCode = "409", description = "El código QR no está vinculado a ningún participante"),
                    @ApiResponse(responseCode = "500", description = "Error inesperado al generar el código QR")
            })
    @GetMapping(GET_QR_IMAGE_BY_ID_FOR_PARTICIPANT)
    @PreAuthorize("hasRole('PARTICIPANT')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> getQrImageByIdForAdmi(@AuthenticationPrincipal UserDetails userDetails)
            throws NotFoundException {
        byte[] qrImage = qrCodeService.getQrImageByQrUsername(userDetails.getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
    }
}
