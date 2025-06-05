package com.compdes.auth.users.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum que define los roles disponibles para los usuarios del sistema.
 * Incluye un nombre técnico y una etiqueta descriptiva.
 * 
 * @param roleName  cadena corta en mayúsculas usada en lógica del sistema (ej.
 *                  "ADMIN")
 * @param roleLabel etiqueta legible asociada al rol (ej.
 *                  "Administrador")
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-01
 */
@AllArgsConstructor
@Getter
public enum RolesEnum {

    ADMIN("Administrador"),
    PARTICIPANT("Participante");

    /**
     * Nombre descriptivo del rol para mostrar en interfaces de usuario.
     */
    private final String roleLabel;

}
