package com.compdes.moduleUni.mappers;

import com.compdes.moduleUni.models.dto.request.CreateModuleUniDTO;
import com.compdes.moduleUni.models.dto.response.ResponseModuleUniDTO;
import com.compdes.moduleUni.models.entities.ModuleUni;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED, unmappedTargetPolicy = ReportingPolicy.IGNORE)
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
     * @param createModuleUniDTO DTO con la información base del participante
     * @return instancia de `Participant` parcialmente mapeada
     */
     public ModuleUni createModuleDtoToModule(CreateModuleUniDTO createModuleUniDTO);

    /**
     * Convierte una entidad `ModuleUni` a un DTO de respuesta.
     */
    public ResponseModuleUniDTO moduleToResponseDto(ModuleUni moduleUni);
}
