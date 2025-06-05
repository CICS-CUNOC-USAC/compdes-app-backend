package com.compdes.participants.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.compdes.participants.models.dto.request.CreateParticipantDTO;
import com.compdes.participants.models.dto.response.ParticipantDTO;
import com.compdes.participants.models.entities.Participant;
import com.compdes.registrationStatus.mappers.RegistrationStatusMapper;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-30
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {
        RegistrationStatusMapper.class })
public interface ParticipantMapper {

    /**
     * Convierte un DTO de creación de participante a una entidad `Participant`.
     * 
     * Este método realiza el mapeo directo entre las propiedades coincidentes
     * del DTO y de la entidad, excluyendo propiedades que no están presentes
     * o no son relevantes en el contexto de creación inicial.
     * 
     * <p>
     * <strong> Propiedades no mapeadas automáticamente:</strong>
     * </p>
     * <ul>
     * <li><strong>Participant:</strong> <code>id</code>, <code>createdAt</code>,
     * <code>updatedAt</code>,
     * <code>deletedAt</code>, <code>desactivatedAt</code>, <code>isAuthor</code>,
     * <code>qrCode</code>, <code>registrationStatus</code></li>
     * <li><strong>PaymentProof:</strong> <code>id</code>, <code>createdAt</code>,
     * <code>updatedAt</code>,
     * <code>deletedAt</code>, <code>desactivatedAt</code>,
     * <code>participant</code></li>
     * </ul>
     * 
     * Estas propiedades deben ser asignadas manualmente desde la lógica de servicio
     * o mediante métodos de mapeo adicionales.
     * 
     * @param createParticipantDTO DTO con la información base del participante
     * @return instancia de `Participant` parcialmente mapeada
     */
    public Participant createParticipantDtoToParticipant(CreateParticipantDTO createParticipantDTO);

    /**
     * Convierte una entidad Participant en un ParticipantDTO.
     *
     * @param participant la entidad Participant a convertir
     * @return el DTO correspondiente
     */
    public ParticipantDTO participantToParticipantDto(Participant participant);

    /**
     * Convierte una lista de entidades Participant en una lista de ParticipantDTOs.
     *
     * @param participants la lista de entidades Participant a convertir
     * @return una lista de DTOs correspondientes
     */
    public List<ParticipantDTO> participantsToParticipantDtos(List<Participant> participants);

}
