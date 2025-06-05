package com.compdes.registrationStatus.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compdes.common.exceptions.DuplicateResourceException;
import com.compdes.common.exceptions.IncompleteDataException;
import com.compdes.common.exceptions.enums.IncompleteDataErrorEnum;
import com.compdes.participants.models.entities.Participant;
import com.compdes.registrationStatus.models.entities.RegistrationStatus;
import com.compdes.registrationStatus.repositories.RegistrationStatusRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de la gestión del estado de registro de los participantes.
 * 
 * Contiene la lógica de negocio relacionada con la creación y validación de
 * estados de registro, asegurando la integridad y consistencia de los datos
 * antes de su persistencia.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-30
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class RegistrationStatusService {

    private final RegistrationStatusRepository registrationStatusRepository;

    /**
     * Crea y guarda un nuevo estado de registro para un participante.
     * 
     * Este método valida que los campos isApproved e isCashPayment no sean nulos,
     * y que el participante no tenga ya un estado de registro asociado.
     * 
     * @param registrationStatus objeto con los datos del estado de registro
     * @param participant        participante al que se asociará el estado de
     *                           registro
     * @return el estado de registro guardado
     * @throws DuplicateResourceException si el participante ya tiene un estado de
     *                                    registro asociado
     * @throws IncompleteDataException    si los campos isApproved o isCashPayment
     *                                    están vacíos
     */
    public RegistrationStatus createRegistrationStatus(RegistrationStatus registrationStatus, Participant participant)
            throws DuplicateResourceException, IncompleteDataException {

        if (registrationStatus.getIsApproved() == null) {
            throw IncompleteDataErrorEnum.REGISTRATION_STATUS_INCOMPLETE.getIncompleteDataException();
        }

        if (registrationStatus.getIsCashPayment() == null && !participant.getIsAuthor()) {
            throw IncompleteDataErrorEnum.NO_AUTHOR_REGISTRATION_STATUS_INCOMPLETE.getIncompleteDataException();
        }

        // verificar que no exista otro registro con el mismo participante
        if (registrationStatusRepository.existsByParticipant(participant)) {
            throw new DuplicateResourceException("El participante ya cuenta con un estado de registro asociado.");
        }

        registrationStatus.setParticipant(participant);

        RegistrationStatus savedStatus = registrationStatusRepository.save(registrationStatus);

        return savedStatus;
    }
}
