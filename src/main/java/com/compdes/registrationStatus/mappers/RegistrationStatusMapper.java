package com.compdes.registrationStatus.mappers;

import org.mapstruct.Mapper;

import com.compdes.registrationStatus.models.dto.response.RegistrationStatusDTO;
import com.compdes.registrationStatus.models.entities.RegistrationStatus;

/**
 * Mapper que convierte una entidad RegistrationStatus en su DTO
 * correspondiente.
 * Utiliza MapStruct para realizar la conversión automáticamente.
 *
 * Esta interfaz está configurada para integrarse con Spring.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-03
 */
@Mapper(componentModel = "spring")
public interface RegistrationStatusMapper {

    /**
     * Convierte una entidad RegistrationStatus en un RegistrationStatusDTO.
     *
     * @param registrationStatus la entidad a convertir
     * @return el DTO correspondiente
     */
    public RegistrationStatusDTO registrationStatusToRegistrationStatusDTO(RegistrationStatus registrationStatus);
}
