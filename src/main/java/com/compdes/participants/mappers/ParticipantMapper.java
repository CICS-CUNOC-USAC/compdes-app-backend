package com.compdes.participants.mappers;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.compdes.common.config.AppProperties;
import com.compdes.participants.models.dto.request.CreateParticipantDTO;
import com.compdes.participants.models.dto.response.AdminParticipantProfileDTO;
import com.compdes.participants.models.dto.response.ParticipantProfileDTO;
import com.compdes.participants.models.dto.response.PublicParticipantProfileDTO;
import com.compdes.participants.models.entities.Participant;
import com.compdes.qrCodes.controllers.QrCodeController;
import com.compdes.registrationStatus.mappers.RegistrationStatusMapper;
import com.compdes.storedFiles.controllers.StoredFileController;

/**
 * Mapper encargado de convertir entidades {@link Participant} a distintos DTOs
 * de presentación.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-30
 */
@Mapper(componentModel = "spring", uses = {
                RegistrationStatusMapper.class })
public abstract class ParticipantMapper {

        @Autowired
        private AppProperties appProperties;

        private final String HOST = "https://compdes.cunoc.edu.gt/api";

        /**
         * Convierte un DTO de creación de participante a una entidad
         * {@link Participant}.
         * 
         * Este método realiza el mapeo directo entre las propiedades coincidentes
         * del DTO y la entidad, excluyendo aquellas propiedades que no son relevantes
         * o no deben ser inicializadas durante la creación.
         * 
         * <p>
         * <strong>Nota:</strong>
         * Se espera qie las propiedades ingoradas sean asignadas manualmente desde la
         * lógica de servicio
         * o mediante métodos de mapeo complementarios.
         * </p>
         * 
         * @param createParticipantDTO DTO con la información base del participante
         * @return {@link Participant} parcialmente mapeada
         */
        public abstract Participant createParticipantDtoToParticipant(CreateParticipantDTO createParticipantDTO);

        /**
         * Convierte una entidad {@link Participant} en un DTO
         * {@link PublicParticipantProfileDTO}
         * que expone únicamente información pública del participante.
         * 
         * 
         * @param participant entidad participante a convertir
         * @return DTO con la información pública del participante
         */
        public abstract PublicParticipantProfileDTO participantToPublicParticipantInfoDto(Participant participant);

        /**
         * Convierte una entidad {@link Participant} en un DTO
         * {@link ParticipantProfileDTO} que expone información detallada y privada del
         * perfil
         * del participante.
         * 
         * @param participant entidad participante a convertir
         * @return DTO con la información del perfil del participante
         */
        @Mapping(target = "qrCodeLink", ignore = true)
        public abstract ParticipantProfileDTO participantToParticipantProfileDto(Participant participant);

        /**
         * Convierte una entidad {@link Participant} en un DTO
         * {@link AdminParticipantProfileDTO} que expone información privada
         * del participante.
         *
         * @param participant entidad participante a mapear
         * @return DTO con la información privada del participante
         */
        @Mapping(target = "qrCodeLink", ignore = true)
        @Mapping(target = "cardPaymentProofLink", ignore = true)
        @Mapping(target = "transferPaymentProofLink", ignore = true)
        @Mapping(target = "isCardPayment", ignore = true)
        @Mapping(target = "isTransferPayment", ignore = true)
        public abstract AdminParticipantProfileDTO participantToPrivateParticipantInfoDto(Participant participant);

        /**
         * Convierte una lista de entidades {@link Participant} en una lista de DTOs
         * {@link AdminParticipantProfileDTO}
         * que contienen información detallada y sensible de cada participante.
         * 
         * @param participant lista de participantes a convertir
         * @return lista de DTOs con información privada de los participantes
         */
        public abstract List<AdminParticipantProfileDTO> participantsToPrivateParticipantInfoDtos(
                        List<Participant> participant);

        /**
         * Agrega información adicional al DTO {@link ParticipantProfileDTO}
         * después del mapeo principal.
         * 
         * @param dto         DTO ya mapeado que será enriquecido
         * @param participant entidad de origen con los datos originales
         */
        @AfterMapping
        protected void enrichParticipantProfileDto(@MappingTarget ParticipantProfileDTO dto, Participant participant) {
                dto.setQrCodeLink(
                                (participant.getQrCode() != null)
                                                ? HOST
                                                                + QrCodeController.BASE_PATH
                                                                + QrCodeController.BASE_GET_QR_IMAGE_BY_ID_FOR_PARTICIPANT
                                                : null);
        }

        /**
         * Agrega información adicional al DTO {@link AdminParticipantProfileDTO} tras
         * el mapeo principal.
         *
         * @param dto         DTO a enriquecer
         * @param participant entidad participante de origen
         */
        @AfterMapping
        protected void enrichWithLinks(@MappingTarget AdminParticipantProfileDTO dto, Participant participant) {

                // marca si el participante tiene un comprobante estructurado (formulario)
                dto.setIsCardPayment((participant.getPaymentProof() != null) ? Boolean.TRUE : Boolean.FALSE);

                // marca si el participante subió una imagen como comprobante de transferencia
                dto.setIsTransferPayment((participant.getPaymentProofImage() != null) ? Boolean.TRUE : Boolean.FALSE);

                // genera el enlace absoluto al código QR, si existe
                dto.setQrCodeLink((participant.getQrCode() != null)
                                ? HOST
                                                + QrCodeController.BASE_PATH
                                                + QrCodeController.BASE_GET_QR_IMAGE_BY_ID_FOR_ADMIN
                                                + "/"
                                                + participant.getQrCode().getId()
                                : null);

                // enlace absoluto al comprobante de tarjeta, si existe
                dto.setCardPaymentProofLink(
                                (participant.getPaymentProof() != null)
                                                ? participant.getPaymentProof().getLink()
                                                : null);

                // enlace absoluto a la imagen de comprobante de transferencia, si existe
                dto.setTransferPaymentProofLink(
                                (participant.getPaymentProofImage() != null)
                                                ? HOST
                                                                + StoredFileController.BASE_PATH
                                                                + StoredFileController.BASE_GET_FILE_BY_ID
                                                                + participant.getPaymentProofImage().getId()
                                                : null);
        }


}
