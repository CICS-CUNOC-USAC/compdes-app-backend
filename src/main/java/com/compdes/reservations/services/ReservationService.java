package com.compdes.reservations.services;

import com.compdes.activity.enums.ActivityType;
import com.compdes.activity.models.entities.Activity;
import com.compdes.activity.repositories.ActivityRepository;
import com.compdes.auth.users.models.entities.CompdesUser;
import com.compdes.common.exceptions.NotFoundException;
import com.compdes.reservations.mappers.ReservationMapper;
import com.compdes.reservations.models.dto.request.ReservationDTO;
import com.compdes.reservations.models.entities.Reservation;
import com.compdes.reservations.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if(activity.getType() != ActivityType.WORKSHOP){
            //throw new
        }
        CompdesUser user = new CompdesUser();
        user.setId(""); //todo TERMINAR

        Reservation reservation = new Reservation(user, activity);
        return reservationRepository.save(reservation);
    }

    /**
     *  Registra una asistencia a un taller
     * */
    public void registerAssistanceToReservation(ReservationDTO reservationDTO){

    }
}
