package com.compdes.reservations.repositories;

import com.compdes.reservations.models.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para operaciones CRUD sobre la entidad {@link Reservation}.
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-07-01
 */
public interface ReservationRepository extends JpaRepository<Reservation, String> {
    long countByActivityId(String activityId);

    Optional<Reservation> findByCompdesUserIdAndActivityId(String compdesUserId, String activityId);

}
