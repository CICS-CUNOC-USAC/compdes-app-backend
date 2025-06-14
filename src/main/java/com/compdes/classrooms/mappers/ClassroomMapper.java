package com.compdes.classrooms.mappers;

import com.compdes.classrooms.models.dto.request.CreateClassroomDTO;
import com.compdes.classrooms.models.dto.response.ResponseClassroomDTO;
import com.compdes.classrooms.models.entities.Classroom;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-30
 */
@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClassroomMapper {
    /**
     * Convierte un DTO de creación de salon a una entidad `Classroom`.
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
     * @param createClassroomDTO DTO con la información base del participante
     * @return instancia de `Participant` parcialmente mapeada
     */
    public Classroom createClassroomDtoToClassroom(CreateClassroomDTO createClassroomDTO);

    /**
    * Convierte una entidad `Classroom` a un DTO de respuesta.
    */
    public ResponseClassroomDTO classroomToResponseDto(Classroom classroom);
}
