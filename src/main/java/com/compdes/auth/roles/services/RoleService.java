package com.compdes.auth.roles.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.compdes.auth.users.enums.RolesEnum;
import com.compdes.common.exceptions.NotFoundException;

/**
 * Servicio que gestiona operaciones relacionadas con los roles de usuario.
 *
 * Utiliza el enum {@link RolesEnum} como fuente central de definición de roles.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-03
 */
@Service
public class RoleService {

    /**
     * Obtiene la lista de todos los roles definidos en el sistema, excluyendo el
     * rol {@code RolesEnum#PARTICIPANT}.
     *
     * @return una lista de elementos del enum {@link RolesEnum} sin incluir
     *         PARTICIPANT
     */
    public List<RolesEnum> findAllRoles() {
        return Arrays.stream(RolesEnum.values())
                .filter(role -> role != RolesEnum.PARTICIPANT)
                .toList();
    }

    /**
     * Busca un rol del sistema basado en su etiqueta descriptiva.
     *
     * @param label la etiqueta legible asociada al rol.
     * @return el valor del enum {@link RolesEnum} correspondiente a la etiqueta
     * @throws NotFoundException si no se encuentra ningún rol con la etiqueta
     *                           proporcionada
     */
    public RolesEnum findRoleByLabel(String label) throws NotFoundException {
        return Arrays.stream(RolesEnum.values())
                .filter(role -> role.getRoleLabel().equals(label))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No se encontró un rol con la etiqueta: " + label));
    }
}
