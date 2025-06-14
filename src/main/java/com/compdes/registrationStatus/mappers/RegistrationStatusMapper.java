package com.compdes.registrationStatus.mappers;

import org.mapstruct.Mapper;

import com.compdes.registrationStatus.models.dto.response.PrivateRegistrationStatusInfoDTO;
import com.compdes.registrationStatus.models.dto.response.PublicRegistrationStatusInfoDTO;
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
     * Convierte una entidad {@link RegistrationStatus} a un DTO
     * {@link PrivateRegistrationStatusInfoDTO},
     * incluyendo información sensible.
     * 
     * @param registrationStatus entidad de estado de registro a convertir
     * @return DTO con información privada del estado de registro
     */
    public PrivateRegistrationStatusInfoDTO registrationStatusToPrivateRegistrationStatusInfoDto(
            RegistrationStatus registrationStatus);

    /**
     * Convierte una entidad {@link RegistrationStatus} a un DTO
     * {@link PublicRegistrationStatusInfoDTO},
     * exponiendo únicamente el estado de aprobación.
     * 
     * Este DTO es adecuado para respuestas públicas o endpoints sin autenticación,
     * ya que no incluye información sensible.
     * 
     * @param registrationStatus entidad de estado de registro a convertir
     * @return DTO con información pública del estado de registro
     */
    public PublicRegistrationStatusInfoDTO registrationStatusToPublicRegistrationStatusInfoDto(
            RegistrationStatus registrationStatus);

}
