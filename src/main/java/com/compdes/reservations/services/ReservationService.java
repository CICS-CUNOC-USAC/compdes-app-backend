package com.compdes.reservations.services;

import com.compdes.activity.enums.ActivityType;
import com.compdes.activity.models.entities.Activity;
import com.compdes.activity.repositories.ActivityRepository;
import com.compdes.auth.users.models.entities.CompdesUser;
import com.compdes.common.exceptions.CustomRuntimeException;
import com.compdes.common.exceptions.NotFoundException;
import com.compdes.common.exceptions.enums.ErrorCodeMessageEnum;
import com.compdes.reservations.mappers.ReservationMapper;
import com.compdes.reservations.models.dto.request.AssistanceToReservationDTO;
import com.compdes.reservations.models.dto.request.ReservationDTO;
import com.compdes.reservations.models.entities.Reservation;
import com.compdes.reservations.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

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

    /**
     * Registra una reservacion para un taller
     */
    public Reservation createReservation(ReservationDTO reservationDTO) throws NotFoundException {
        Activity activity = activityRepository.findById(reservationDTO.getActivityId())
                .orElseThrow(() -> new NotFoundException("Taller no encontrado"));
        validateActivityToReserve(activity);

        //TODO obtener los datos del usuario logueado
        //TODO y validar si no se translapa con otra asignacion

        //JwtTokenInspector jwtTokenInspector = new JwtTokenInspector();
        CompdesUser user = new CompdesUser();
        user.setId(""); //todo TERMINAR

        Reservation reservation = new Reservation(user, activity);
        return reservationRepository.save(reservation);
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
     * */
    public void registerAssistanceToReservation(AssistanceToReservationDTO assistanceToReservationDTO){

    }

    /**
     * Cancela una reservación a un taller
     * */
    public Reservation cancelReservation(ReservationDTO reservationDTO) throws NotFoundException {
        Activity activity = activityRepository.findById(reservationDTO.getActivityId())
                .orElseThrow(() -> new NotFoundException("Taller no encontrado"));

        Optional<Reservation> reservationOp = reservationRepository
                .findByCompdesUserIdAndActivityId("compdesUserID", activity.getId()); //TODO find id
        if(reservationOp.isEmpty()){
            throw new NotFoundException("Reservacion no encontrada")
        }

        if(LocalDateTime.now().isAfter(activity.getInitScheduledDate())){
            throw new CustomRuntimeException(
                    ErrorCodeMessageEnum.INVALID_DATE_RANGE.getCode(),
                    "El taller ya se impartió o se está impartiendo, ya no se puede cancelar la reservacion"
            );
        }

        Reservation reservation = reservationOp.get();
        reservation.setAttendedDateTime(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }


}
