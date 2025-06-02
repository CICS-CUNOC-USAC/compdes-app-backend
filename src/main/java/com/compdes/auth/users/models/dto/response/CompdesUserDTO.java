package com.compdes.auth.users.models.dto.response;

import lombok.Value;

/**
 * DTO de solo lectura utilizado para representar datos públicos de un usuario
 * del sistema.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-01
 */
@Value
public class CompdesUserDTO {

    String username;
    String role;
}
