package com.compdes.common.exceptions.enums;

import com.compdes.common.exceptions.QrCodeException;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * Enumeración de errores específicos relacionados con la gestión de códigos QR.
 * 
 * Cada constante representa un caso particular de error asociado a operaciones
 * con códigos QR, encapsulando una instancia de {@link QrCodeException} con su
 * respectivo código y mensaje definido en {@link ErrorCodeMessageEnum}.
 * 
 * Esta estructura permite centralizar el control de errores QR, facilitando su
 * reutilización coherente en la lógica de negocio.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-06
 */
@Getter
@AllArgsConstructor
public enum QrCodeErrorEnum {

        NO_AVAILABLE_QR_CODE(new QrCodeException(ErrorCodeMessageEnum.NO_AVAILABLE_QR_CODE.getCode(),
                        ErrorCodeMessageEnum.NO_AVAILABLE_QR_CODE.getMessage())),
        QR_ALREADY_ASSIGNED(new QrCodeException(
                        ErrorCodeMessageEnum.QR_ALREADY_ASSIGNED.getCode(),
                        ErrorCodeMessageEnum.QR_ALREADY_ASSIGNED.getMessage())),
        PARTICIPANT_ALREADY_HAS_QR(new QrCodeException(
                        ErrorCodeMessageEnum.PARTICIPANT_ALREADY_HAS_QR.getCode(),
                        ErrorCodeMessageEnum.PARTICIPANT_ALREADY_HAS_QR.getMessage())),
        QR_GENERATION_FAILED(new QrCodeException(
                        ErrorCodeMessageEnum.QR_GENERATION_FAILED.getCode(),
                        ErrorCodeMessageEnum.QR_GENERATION_FAILED.getMessage())),
        QR_IMAGE_ENCODING_FAILED(new QrCodeException(
                        ErrorCodeMessageEnum.QR_IMAGE_ENCODING_FAILED.getCode(),
                        ErrorCodeMessageEnum.QR_IMAGE_ENCODING_FAILED.getMessage())),
                        ;

        private final QrCodeException qrCodeException;
}
