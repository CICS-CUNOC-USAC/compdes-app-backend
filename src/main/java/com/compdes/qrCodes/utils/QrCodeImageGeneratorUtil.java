package com.compdes.qrCodes.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.compdes.common.exceptions.QrCodeException;
import com.compdes.common.exceptions.enums.QrCodeErrorEnum;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Utilidad encargada de generar imágenes QR en formato PNG.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-13
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class QrCodeImageGeneratorUtil {

    /**
     * Genera un código QR en formato PNG para el identificador de un participante.
     * 
     * Este método codifica el {@code participantId} como contenido del código QR
     * y lo deroga la creaxion en una imagen PNG en forma de arreglo de bytes.
     * 
     * @param qrCodeId identificador único del participante que se codificará
     *                      en el QR
     * @return arreglo de bytes que representa la imagen PNG del código QR generado
     * @throws QrCodeException si ocurre un error durante la generación o escritura
     *                         de la imagen
     */
    public byte[] generateQrCode(String qrCodeId) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;
        try {
            // genera la matriz QR con el contenido del ID del participante
            bitMatrix = qrCodeWriter.encode(qrCodeId, BarcodeFormat.QR_CODE, 200, 200);
            return convertBitMatrixToPngByteArray(bitMatrix);
        } catch (WriterException e) {
            // registra el error si no se pudo generar el QR
            log.error("Error al generar el código QR para el contenido '{}': {}", qrCodeId, e.getMessage(), e);
            throw QrCodeErrorEnum.QR_GENERATION_FAILED.getQrCodeException();
        } catch (IOException e) {
            // registra el error si no se pudo escribir la imagen en el stream
            log.error("Error al escribir la imagen QR en stream (PNG) para '{}': {}", qrCodeId, e.getMessage(), e);
            throw QrCodeErrorEnum.QR_IMAGE_ENCODING_FAILED.getQrCodeException();
        }
    }

    /**
     * Convierte una matriz de código QR en una imagen PNG como arreglo de bytes.
     * 
     * @param bitMatrix la matriz generada del código QR
     * @return arreglo de bytes que representa la imagen PNG
     * @throws IOException si ocurre un error al escribir en el stream
     */
    private byte[] convertBitMatrixToPngByteArray(BitMatrix bitMatrix) throws IOException {
        // crea un stream para almacenar la imagen en formato PNG
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();

        // escribe la matriz QR como imagen PNG en el stream
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        // retorna el contenido del stream como arreglo de bytes
        return pngOutputStream.toByteArray();
    }

}
