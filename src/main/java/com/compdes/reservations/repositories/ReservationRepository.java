package com.compdes.reservations.repositories;

import com.compdes.reservations.models.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Repositorio para operaciones CRUD sobre la entidad {@link Reservation}.
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-07-01
 */
public interface ReservationRepository extends JpaRepository<Reservation, String> {
    long countByActivityId(String activityId);

    Optional<Reservation> findByParticipantIdAndActivityId(String participantId, String activityId);

    List<Reservation> findByParticipantId(String participantId);

    @Query("""
    SELECT COUNT(*) FROM Reservation r
    WHERE r.participant.id = :participantId
      AND (
          r.activity.initScheduledDate < :end
          AND r.activity.endScheduledDate > :start
      )
    """)
    Long countOverlappingReservations(
            @Param("participantId") String participantId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}
