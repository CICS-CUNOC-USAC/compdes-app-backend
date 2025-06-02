package com.compdes.auth.users.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.compdes.auth.users.models.dto.request.CreateCompdesUserDTO;
import com.compdes.auth.users.models.dto.response.CompdesUserDTO;
import com.compdes.auth.users.models.entities.CompdesUser;
import com.compdes.common.models.entities.Auditor;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-01
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompdesUserMapper {

    /**
     * Convierte un DTO de creación de usuario en una entidad {@link CompdesUser}.
     * 
     * Este método realiza un mapeo directo de los campos incluidos en el DTO
     * (username, password, role)
     * hacia la entidad de dominio, dejando sin asignar aquellos campos que deben
     * ser
     * gestionados por el sistema u otros componentes (como auditoría o generación
     * de ID).
     * 
     * <p>
     * <strong>Campos no mapeados automáticamente:</strong>
     * </p>
     * <ul>
     * <li><code>id</code> – generado automáticamente por la base de datos o por
     * lógica de servicio</li>
     * <li><code>createdAt</code>, <code>updatedAt</code> – asignados por
     * {@link Auditor} vía auditoría</li>
     * <li><code>deletedAt</code>, <code>desactivatedAt</code> – manejados por
     * lógica de eliminación o inactivación</li>
     * </ul>
     * 
     * Estos campos deben establecerse fuera del mapper, usualmente desde el
     * servicio o el motor de persistencia,
     * por lo tanto es esperado y seguro que MapStruct los ignore.
     * 
     * @param createCompdesUserDTO DTO con los datos del nuevo usuario
     * @return entidad {@link CompdesUser} con los campos principales mapeados
     */
    public CompdesUser createCompdesUserDtoToCompdesUser(CreateCompdesUserDTO createCompdesUserDTO);

    public CompdesUserDTO compdesUserToCompdesUserDTO(CompdesUser compdesUser);
}
