package com.compdes.registrationStatus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compdes.participants.models.entities.Participant;
import com.compdes.registrationStatus.models.entities.RegistrationStatus;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-30
 */
@Repository
public interface RegistrationStatusRepository extends JpaRepository<RegistrationStatus, String> {

    public Boolean existsByParticipant(Participant participant);
}
