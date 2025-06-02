package com.compdes.common.enums;

import lombok.Getter;

/**
 * Enum que centraliza las rutas públicas (endpoints) que no requieren
 * autenticación JWT.
 * 
 * Estas rutas son permitidas explícitamente en la configuración de seguridad
 * y deben coincidir con los paths definidos en los controladores que se desea
 * excluir del filtro de autenticación.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-31
 */
@Getter
public enum PublicEndpointsEnum {

    AUTH_LOGIN("/api/v1/auth/login"),
    AUTH_REGISTER("/api/auth/register"),
    SWAGGER_UI("/swagger-ui"),
    API_DOCS("/v3/api-docs");

    private final String path;

    PublicEndpointsEnum(String path) {
        this.path = path;
    }

}
