package com.compdes.common.exceptions.enums;

import com.compdes.common.exceptions.InvalidTokenException;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeración de errores relacionados con la validación de tokens JWT.
 * 
 * Cada constante representa un tipo específico de fallo que puede ocurrir
 * durante el procesamiento de un token JWT, encapsulando una instancia de
 * {@link InvalidTokenException} con su código y mensaje correspondiente
 * definidos en {@link ErrorCodeMessageEnum}.
 * 
 * Esta enumeración permite manejar de forma estructurada y reutilizable
 * las distintas causas de errores al validar tokens, facilitando su integración
 * con el controlador global de excepciones.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-27
 */
@Getter
@AllArgsConstructor
public enum JwtErrorEnum {

        JWT_INVALID(new InvalidTokenException(ErrorCodeMessageEnum.JWT_INVALID.getCode(),
                        ErrorCodeMessageEnum.JWT_INVALID.getCode())),

        JWT_NO_EXPIRATION(
                        new InvalidTokenException(ErrorCodeMessageEnum.JWT_NO_EXPIRATION.getCode(),
                                        ErrorCodeMessageEnum.JWT_NO_EXPIRATION.getCode())),
        JWT_NO_CLIENT_ID(
                        new InvalidTokenException(ErrorCodeMessageEnum.JWT_NO_CLIENT_ID.getCode(),
                                        ErrorCodeMessageEnum.JWT_NO_CLIENT_ID.getCode())),

        JWT_NO_AUTHORITIES(
                        new InvalidTokenException(ErrorCodeMessageEnum.JWT_NO_AUTHORITIES.getCode(),
                                        ErrorCodeMessageEnum.JWT_NO_AUTHORITIES.getCode())),

        JWT_NO_USER_TYPE(
                        new InvalidTokenException(ErrorCodeMessageEnum.JWT_NO_USER_TYPE.getCode(),
                                        ErrorCodeMessageEnum.JWT_NO_USER_TYPE.getCode())),

        JWT_NO_USERNAME(
                        new InvalidTokenException(ErrorCodeMessageEnum.JWT_NO_USERNAME.getCode(),
                                        ErrorCodeMessageEnum.JWT_NO_USERNAME.getCode())),

        CLAIM_TYPE_MISMATCH(
                        new InvalidTokenException(ErrorCodeMessageEnum.CLAIM_TYPE_MISMATCH.getCode(),
                                        ErrorCodeMessageEnum.CLAIM_TYPE_MISMATCH.getCode())),

        JWT_UNSUPPORTED(new InvalidTokenException(ErrorCodeMessageEnum.JWT_UNSUPPORTED.getCode(),
                        ErrorCodeMessageEnum.JWT_UNSUPPORTED.getMessage())),
        JWT_MALFORMED(new InvalidTokenException(ErrorCodeMessageEnum.JWT_MALFORMED.getCode(),
                        ErrorCodeMessageEnum.JWT_MALFORMED.getMessage())),
        JWT_SIGNATURE_INVALID(new InvalidTokenException(ErrorCodeMessageEnum.JWT_SIGNATURE_INVALID.getCode(),
                        ErrorCodeMessageEnum.JWT_SIGNATURE_INVALID.getMessage())),
        JWT_EXPIRED(new InvalidTokenException(ErrorCodeMessageEnum.JWT_EXPIRED.getCode(),
                        ErrorCodeMessageEnum.JWT_EXPIRED.getMessage())),
        JWT_ILLEGAL_ARGUMENT(new InvalidTokenException(ErrorCodeMessageEnum.JWT_ILLEGAL_ARGUMENT.getCode(),
                        ErrorCodeMessageEnum.JWT_ILLEGAL_ARGUMENT.getMessage()));

        private final InvalidTokenException invalidTokenException;

}
