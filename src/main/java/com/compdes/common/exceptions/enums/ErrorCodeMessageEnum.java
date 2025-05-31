package com.compdes.common.exceptions.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum que contiene los códigos y mensajes de error para operaciones de la
 * aplicacion.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-27
 */
@AllArgsConstructor
@Getter
public enum ErrorCodeMessageEnum {
    // --- Errores JWT ---
    JWT_INVALID("JWT-001", "El token JWT es inválido o ha sido manipulado"),
    JWT_NO_EXPIRATION("JWT-002", "El token JWT no contiene fecha de expiración"),
    JWT_NO_CLIENT_ID("JWT-003", "No se pudo extraer el clientId del token JWT"),
    JWT_NO_AUTHORITIES("JWT-004", "No se pudo extraer los permisos del usuario del token JWT"),
    CLAIM_TYPE_MISMATCH("JWT-005", "El valor de la claim no coincide con el tipo esperado"),
    JWT_NO_USER_TYPE("JWT-006", "El token JWT no contiene el tipo de usuario"),
    JWT_NO_USERNAME("JWT-007", "El token JWT no contiene el nombre de usuario"),
    JWT_UNSUPPORTED("JWT-008", "El token JWT tiene un formato no soportado"),
    JWT_MALFORMED("JWT-009", "El token JWT está malformado"),
    JWT_SIGNATURE_INVALID("JWT-010", "La firma del token JWT no es válida"),
    JWT_EXPIRED("JWT-011", "El token JWT ha expirado"),
    JWT_ILLEGAL_ARGUMENT("JWT-012", "El token JWT está vacío o contiene solo espacios"),

    // errores en registro
    REGISTRATION_STATUS_INCOMPLETE("REG-001",
            "Los campos 'isApproved' e 'isCashPayment' no pueden ser nulos al crear un estado de registro.");

    private final String code;
    private final String message;

}
