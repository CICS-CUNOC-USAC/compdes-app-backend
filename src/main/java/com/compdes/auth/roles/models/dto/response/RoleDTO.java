package com.compdes.auth.roles.models.dto.response;

import lombok.Value;

/**
 * DTO utilizado para exponer los roles disponibles en el sistema a través de la
 * API.
 * Contiene únicamente la etiqueta legible del rol, destinada a interfaces de
 * usuario.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-04
 */
@Value
public class RoleDTO {

    String label;
}
