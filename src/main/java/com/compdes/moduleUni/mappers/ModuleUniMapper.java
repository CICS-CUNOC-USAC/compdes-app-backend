package com.compdes.moduleUni.mappers;

import com.compdes.classrooms.models.entities.Classroom;
import com.compdes.moduleUni.models.dto.request.CreateModuleDTO;
import com.compdes.moduleUni.models.dto.response.ResponseModuleDTO;
import com.compdes.moduleUni.models.entities.ModuleUni;

public interface ModuleUniMapper {
    /**
     * Convierte un DTO de creación de module a una entidad `ModuleUni`.
     *
     * Este método realiza el mapeo directo entre las propiedades coincidentes
     * del DTO y de la entidad, excluyendo propiedades que no están presentes
     * o no son relevantes en el contexto de creación inicial.
     *
     * <p>
     * <strong> Propiedades no mapeadas automáticamente:</strong>
     * </p>
     * <ul>
     * <code>updatedAt</code>,
     * <code>deletedAt</code>,
     * <code>desactivatedAt</code>,
     * </ul>
     *
     * Estas propiedades deben ser asignadas manualmente desde la lógica de servicio
     * o mediante métodos de mapeo adicionales.
     *
     * @param createModuleDTO DTO con la información base del participante
     * @return instancia de `Participant` parcialmente mapeada
     */
     public ModuleUni createModuleDtoToModule(CreateModuleDTO createModuleDTO);

    /**
     * Convierte una entidad `ModuleUni` a un DTO de respuesta.
     */
    public ResponseModuleDTO moduleToResponseDto(ModuleUni moduleUni);
}
