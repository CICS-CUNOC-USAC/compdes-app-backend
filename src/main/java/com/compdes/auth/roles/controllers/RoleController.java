package com.compdes.auth.roles.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.compdes.auth.roles.mappers.RoleMapper;
import com.compdes.auth.roles.models.dto.response.RoleDTO;
import com.compdes.auth.roles.services.RoleService;
import com.compdes.auth.users.enums.RolesEnum;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    /**
     * Endpoint que retorna todos los roles disponibles en el sistema, excluyendo
     * los no asignables manualmente
     * como el rol 'PARTICIPANT'.
     *
     * Este recurso se utiliza comúnmente para llenar formularios o interfaces donde
     * se requiere seleccionar
     * un rol legible por el usuario.
     *
     * @return lista de {@link RoleDTO} con las etiquetas de los roles disponibles
     */
    @Operation(summary = "Obtener todos los roles disponibles", description = "Retorna una lista de roles expuestos por el sistema en formato legible para su uso en formularios o interfaces de usuario. El rol 'PARTICIPANT' no se incluye.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de roles obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno al intentar obtener los roles")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<RoleDTO> getAllRoles() {
        List<RolesEnum> rolesEnums = roleService.findAllRoles();
        return roleMapper.rolesEnumListtoRoleDTOList(rolesEnums);
    }

}
