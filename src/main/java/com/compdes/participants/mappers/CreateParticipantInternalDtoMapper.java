package com.compdes.participants.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.compdes.participants.models.dto.internal.CreateParticipantInternalDTO;
import com.compdes.participants.models.dto.request.CreateParticipantWithPaymentDTO;

/**
 * Mapper encargado de transformar objetos de entrada provenientes de la API
 * relacionados con participantes no autores en objetos internos utilizados
 * por la lógica de negocio.
 * 
 * Este mapper convierte instancias de {@link CreateParticipantWithPaymentDTO}
 * en {@link CreateParticipantInternalDTO}, permitiendo desacoplar la estructura
 * externa de la API de la lógica interna del sistema.
 * 
 * Se utiliza MapStruct para generar automáticamente la implementación del
 * mapeo.
 * La estrategia {@code componentModel = "spring"} permite que este mapper
 * sea gestionado como un bean de Spring.
 * 
 * El campo {@code paymentProofImageMultipartFile} es excluido intencionalmente
 * del mapeo, ya que su asignación se realiza posteriormente en una capa de
 * servicio, donde se gestiona el archivo de imagen adjunto como comprobante
 * de pago.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-03
 */

@Mapper(componentModel = "spring")
public interface CreateParticipantInternalDtoMapper {

    /**
     * Convierte un {@link CreateParticipantWithPaymentDTO} (DTO recibido en la API) a
     * un {@link CreateParticipantInternalDTO}, que es utilizado
     * internamente
     * por el sistema para separar responsabilidades y enriquecer con datos
     * adicionales.
     *
     * El campo {@code paymentProofImage} es ignorado explícitamente en este mapeo
     * porque no forma parte del objeto expuesto en la API externa y se espera
     * que sea asignado manualmente en una capa de servicio posterior.
     *
     * @param createNonAuthorParticipantDTO objeto recibido desde el cliente
     * @return DTO interno enriquecido con los datos base del participante no autor
     */
    @Mapping(target = "paymentProofImageMultipartFile", ignore = true)
    public CreateParticipantInternalDTO createParticipantWithPaymentDtoToCreateParticipantInternalDTO(
            CreateParticipantWithPaymentDTO createNonAuthorParticipantDTO);

}
