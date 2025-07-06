package com.compdes.reservations.services;

import com.compdes.activity.enums.ActivityType;
import com.compdes.activity.models.entities.Activity;
import com.compdes.activity.repositories.ActivityRepository;
import com.compdes.auth.users.models.entities.CompdesUser;
import com.compdes.auth.users.services.CompdesUserService;
import com.compdes.common.exceptions.CustomRuntimeException;
import com.compdes.common.exceptions.NotFoundException;
import com.compdes.common.exceptions.enums.ErrorCodeMessageEnum;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.services.ParticipantService;
import com.compdes.reservations.mappers.ReservationMapper;
import com.compdes.reservations.models.dto.request.AssistanceToReservationDTO;
import com.compdes.reservations.models.dto.request.ReservationDTO;
import com.compdes.reservations.models.entities.Reservation;
import com.compdes.reservations.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * Servicio encargado de gestionar la creación y persistencia de reservaciones
 * de un participante para un taller.
 *
 * Administra el registro de reservaciones para talleres, validando la integridad
 * de datos
 *
 * Las operaciones están transaccionalmente garantizadas, realizando rollback en
 * caso de excepción.
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-07-01
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;

    private final ActivityRepository activityRepository;

    private final ParticipantService participantService;
    private final CompdesUserService compdesUserService;

    /**
     * Registra una reservacion para un taller
     */
    public Reservation createReservation(ReservationDTO reservationDTO) throws NotFoundException {
        Activity activity = activityRepository.findById(reservationDTO.getActivityId())
                .orElseThrow(() -> new NotFoundException("Taller no encontrado"));
        validateActivityToReserve(activity);

        CompdesUser user = compdesUserService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        validateOverlappingReservation(activity, user.getParticipant());
        Reservation reservation = new Reservation(user.getParticipant(), activity);
        return reservationRepository.save(reservation);
    }

    private void validateOverlappingReservation(Activity activity, Participant participant){
        if(reservationRepository.countOverlappingReservations(
                participant.getId(), activity.getInitScheduledDate(), activity.getEndScheduledDate()) > 0
        ){
            throw new CustomRuntimeException(
                    ErrorCodeMessageEnum.INVALID_SCHEDULE_EXCEPTION.getCode(),
                    ErrorCodeMessageEnum.INVALID_SCHEDULE_EXCEPTION.getMessage()
            );
        }
    }

    private void validateActivityToReserve(Activity activity){
        if(activity.getType() != ActivityType.WORKSHOP){
            throw new CustomRuntimeException(
                    ErrorCodeMessageEnum.NO_WORKSHOP_EXCEPTION.getCode(),
                    ErrorCodeMessageEnum.NO_WORKSHOP_EXCEPTION.getMessage()
            );
        }
        if(LocalDateTime.now().isAfter(activity.getInitScheduledDate())){
            throw new CustomRuntimeException(
                    ErrorCodeMessageEnum.INVALID_DATE_RANGE.getCode(),
                    "El taller ya se impartió o se está impartiendo, ya no se puede reservar"
            );
        }
        if(reservationRepository.countByActivityId(activity.getId()) >= activity.getClassroom().getCapacity()){
            throw new CustomRuntimeException(
                    ErrorCodeMessageEnum.NO_SPACE_EXCEPTION.getCode(),
                    ErrorCodeMessageEnum.NO_SPACE_EXCEPTION.getMessage()
            );
        }
    }


    /**
     *  Registra una asistencia a un taller
     *  a partir de un qr
     * */
    public Reservation registerAssistanceToReservation(AssistanceToReservationDTO assistanceToReservationDTO)
            throws NotFoundException {
        Participant participant  = participantService.getParticipantByQrCodeId(assistanceToReservationDTO.getQrId());
        Activity activity = activityRepository.findById(assistanceToReservationDTO.getActivityId())
                .orElseThrow(() -> new NotFoundException("Taller no encontrado"));
        Reservation reservation =  reservationRepository.findByParticipantIdAndActivityId(participant.getId(), activity.getId())
                .orElseThrow(() -> new NotFoundException("Reservacion para el taller no encontrada"));

        //verificar que las marcas de tiempo para registrar asistencia este bien
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fifteenBefore = now.minusMinutes(15);
        Long count = reservationRepository.countRegistrationsInWindow(participant.getId(), now, fifteenBefore);
        boolean canRegister = count > 0;
        if(canRegister){
            throw new CustomRuntimeException(
                    ErrorCodeMessageEnum.INVALID_DATE_RANGE.getCode(),
                    "El taller ya pasó de la hora programada, no se puede registrar asistencia"
            );
        }
        reservation.setAttendedDateTime(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    /**
     * Cancela una reservación a un taller
     * */
    public void cancelReservation(ReservationDTO reservationDTO) throws NotFoundException {
        Activity activity = activityRepository.findById(reservationDTO.getActivityId())
                .orElseThrow(() -> new NotFoundException("Taller no encontrado"));

        CompdesUser user = compdesUserService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Reservation reservation = reservationRepository.findByParticipantIdAndActivityId(user.getParticipant().getId(), activity.getId())
                .orElseThrow(() -> new NotFoundException("Reservacion no encontrada"));

        if(LocalDateTime.now().isAfter(activity.getInitScheduledDate())){
            throw new CustomRuntimeException(
                    ErrorCodeMessageEnum.INVALID_DATE_RANGE.getCode(),
                    "El taller ya se impartió o se está impartiendo, ya no se puede cancelar la reservacion"
            );
        }
        reservationRepository.delete(reservation);
    }


}
