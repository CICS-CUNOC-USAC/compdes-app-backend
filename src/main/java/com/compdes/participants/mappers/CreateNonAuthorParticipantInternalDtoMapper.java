package com.compdes.participants.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.compdes.participants.models.dto.internal.CreateNonAuthorParticipantInternalDTO;
import com.compdes.participants.models.dto.request.CreateNonAuthorParticipantDTO;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-03
 */
@Mapper(componentModel = "spring")
public interface CreateNonAuthorParticipantInternalDtoMapper {

    /**
     * Convierte un {@link CreateNonAuthorParticipantDTO} (DTO recibido en la API) a
     * un {@link CreateNonAuthorParticipantInternalDTO}, que es utilizado
     * internamente
     * por el sistema para separar responsabilidades y enriquecer con datos
     * adicionales.
     *
     * El campo {@code paymentProofImage} es ignorado explícitamente en este mapeo
     * porque no forma parte del objeto expuesto en la API externa y se espera
     * que sea asignado manualmente en una capa de servicio posterior, a partir de
     * un archivo recibido por separado (por ejemplo, vía MultipartFile).
     *
     * @param createNonAuthorParticipantDTO objeto recibido desde el cliente
     * @return DTO interno enriquecido con los datos base del participante no autor
     */
    @Mapping(target = "paymentProofImage", ignore = true)
    public CreateNonAuthorParticipantInternalDTO createNonAuthorParticipantDtoToCreateNonAuthorParticipantInternalDTO(
            CreateNonAuthorParticipantDTO createNonAuthorParticipantDTO);

}
