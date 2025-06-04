package com.compdes.auth.login.models.dto.response;

import lombok.Value;

/**
 * DTO que representa la respuesta al iniciar sesión.
 * Contiene el nombre del usuario, su rol y el token JWT generado.
 *
 * Esta clase es inmutable y utiliza Lombok para generar automáticamente
 * constructor, getters, equals, hashCode y toString.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-03
 */
@Value
public class LoginResponseDTO {

    String userName;
    String role;
    String token;
}
