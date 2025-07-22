package com.compdes.reservations.mappers;

import com.compdes.participants.mappers.ParticipantMapper;
import com.compdes.reservations.models.dto.response.ReservationParticipantsDTO;
import com.compdes.reservations.models.dto.response.ReservationResponseDTO;
import com.compdes.reservations.models.entities.Reservation;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import java.util.*;

/**
 *
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-07-01
 */
@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = ParticipantMapper.class
)
public interface ReservationMapper {
    public ReservationResponseDTO reservationToDTO(Reservation reservation);
    public List<ReservationResponseDTO> reservationToDTO(List<Reservation> reservations);

    public ReservationParticipantsDTO reservationToParticipantsDTO(Reservation reservation);
    public List<ReservationParticipantsDTO> reservationToParticipantsDTO(List<Reservation> reservations);
}
