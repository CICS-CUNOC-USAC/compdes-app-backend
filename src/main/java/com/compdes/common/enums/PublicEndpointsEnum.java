package com.compdes.common.enums;

import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum que centraliza las rutas públicas (endpoints) que no requieren
 * autenticación JWT.
 * 
 * Cada entrada representa una combinación específica de método HTTP y path que
 * será explícitamente permitida en la configuración de seguridad de Spring
 * Security.
 * 
 * Si una misma ruta debe permitir múltiples métodos HTTP (por ejemplo, GET y
 * POST),
 * se debe declarar una entrada independiente por cada método.
 * 
 * Es importante que los valores definidos aquí coincidan exactamente con los
 * paths utilizados en los controladores correspondientes, de lo contrario,
 * no serán excluidos correctamente del filtro de autenticación.<br>
 * <p>
 * Los valores de `path` pueden contener comodines usando el estilo de patrones
 * Ant {@link AntPathMatcher}
 * </p>
 * 
 * <li>Ejemplos de uso:</li>
 * 
 * <pre>
 *     // Permitir POST en login
 *     AUTH_LOGIN(HttpMethod.POST, "/api/v1/login")
 *
 *     // Permitir GET y POST en /participants
 *     PARTICIPANT_CREATE_GET(HttpMethod.GET, "/api/v1/participants")
 *     PARTICIPANT_CREATE_POST(HttpMethod.POST, "/api/v1/participants")
 *
 *     // Permitir todos los métodos (dejar como null)
 *     SWAGGER_UI(null, "/swagger-ui/**")
 * </pre>
 * 
 * 
 * @author Luis Monterroso
 * @version 1.1
 * @since 2025-05-31
 * @see AntPathMatcher
 */
@Getter
@AllArgsConstructor
public enum PublicEndpointsEnum {

    AUTH_LOGIN(HttpMethod.POST, "/api/v1/login"),
    PARTICIPANT_CREATE(HttpMethod.POST, "/api/v1/participants"),
    PARTICIPANT_AUTHOR_CREATE(HttpMethod.POST, "/api/v1/participants/author"),
    PUBLIC_INSCRIPTION_BY_DOCUMENT(HttpMethod.GET, "/api/v1/participants/public-inscription/by-document/*"),
    FINALIZE_PARTICIPANT_USER_CREATION(HttpMethod.PATCH, "/api/v1/users/finalize/*"),
    // Rutas publicas de Activity
    ACTIVITY_GET_ALL(HttpMethod.GET, "/api/v1/activities/all"),
    SWAGGER_UI(null, "/swagger-ui/**"),
    API_DOCS(null, "/v3/api-docs/**"),
    //rutas publicas de reservation
    RESERVATION_COUNT_PARTICIPANTS(HttpMethod.GET, "/api/v1/reservations/countParticipants/*"),
    RESERVATION_VERIFY_ASSISTANT(HttpMethod.GET, "/api/v1/reservations/isAssigned/**")
    ;

    private final HttpMethod method;
    private final String path;

}
