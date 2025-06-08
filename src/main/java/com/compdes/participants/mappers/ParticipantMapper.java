package com.compdes.participants.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
@Mapper(componentModel = "spring", uses = {
        RegistrationStatusMapper.class })
public interface ParticipantMapper {

    /**
     * Convierte un DTO de creación de participante a una entidad
     * {@link Participant}.
     * 
     * Este método realiza el mapeo directo entre las propiedades coincidentes
     * del DTO y la entidad, excluyendo aquellas propiedades que no son relevantes
     * o no deben ser inicializadas durante la creación.
     * 
     * <p>
     * <strong>Propiedades ignoradas explícitamente:</strong>
     * </p>
     * <ul>
     * <li><code>createdAt</code></li>
     * <li><code>deletedAt</code></li>
     * <li><code>desactivatedAt</code></li>
     * <li><code>id</code></li>
     * <li><code>updatedAt</code></li>
     * <li><code>compdesUser</code></li>
     * <li><code>isGuest</code></li>
     * <li><code>paymentProof</code></li>
     * <li><code>paymentProofImage</code></li>
     * <li><code>qrCode</code></li>
     * <li><code>registrationStatus</code></li>
     * </ul>
     * 
     * Estas propiedades deben ser asignadas manualmente desde la lógica de servicio
     * o mediante métodos de mapeo complementarios.
     * 
     * @param createParticipantDTO DTO con la información base del participante
     * @return instancia de {@link Participant} parcialmente mapeada
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "desactivatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "compdesUser", ignore = true)
    @Mapping(target = "isGuest", ignore = true)
    @Mapping(target = "paymentProof", ignore = true)
    @Mapping(target = "paymentProofImage", ignore = true)
    @Mapping(target = "qrCode", ignore = true)
    @Mapping(target = "registrationStatus", ignore = true)
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
