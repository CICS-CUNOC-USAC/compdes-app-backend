package com.compdes.registrationStatus.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compdes.auth.users.models.entities.CompdesUser;
import com.compdes.auth.users.services.CompdesUserService;
import com.compdes.common.exceptions.DuplicateResourceException;
import com.compdes.common.exceptions.IncompleteDataException;
import com.compdes.common.exceptions.NotFoundException;
import com.compdes.common.exceptions.QrCodeException;
import com.compdes.common.exceptions.enums.IncompleteDataErrorEnum;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.services.ParticipantService;
import com.compdes.registrationStatus.models.entities.RegistrationStatus;
import com.compdes.registrationStatus.repositories.RegistrationStatusRepository;

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
public class RegistrationStatusService {

    private final RegistrationStatusRepository registrationStatusRepository;
    private final CompdesUserService compdesUserService;
    private final ParticipantService participantService;

    public RegistrationStatusService(RegistrationStatusRepository registrationStatusRepository,
            @Lazy CompdesUserService compdesUserService, @Lazy ParticipantService participantService) {
        this.registrationStatusRepository = registrationStatusRepository;
        this.compdesUserService = compdesUserService;
        this.participantService = participantService;
    }

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

        if (registrationStatus.getIsCashPayment() == null && !participant.getIsGuest()) {
            throw IncompleteDataErrorEnum.NO_AUTHOR_REGISTRATION_STATUS_INCOMPLETE.getIncompleteDataException();
        }

        // verificar que no exista otro registro con el mismo participante
        if (registrationStatusRepository.existsByParticipant(participant)) {
            throw new DuplicateResourceException("El participante ya cuenta con un estado de registro asociado.");
        }

        if (registrationStatusRepository
                .existsByVoucherNumberIsNotNullAndVoucherNumber(registrationStatus.getVoucherNumber())) {
            throw new DuplicateResourceException(
                    "Ya existe un participante con el número de talonario proporcionado.");
        }

        registrationStatus.setParticipant(participant);

        RegistrationStatus savedStatus = registrationStatusRepository.save(registrationStatus);

        return savedStatus;
    }

    /**
     * Aprueba el estado de registro asociado a un participante específico.
     * 
     * Este método busca el estado de registro correspondiente al ID del
     * participante, verifica si ya ha sido aprobado y, si no lo ha sido, lo aprueba
     * y guarda los cambios en la base de datos.
     * 
     * @param participantId identificador del participante cuyo estado de registro
     *                      se desea aprobar
     * @throws NotFoundException     si no se encuentra un estado de registro
     *                               asociado al participante
     * @throws IllegalStateException si el estado de registro ya ha sido aprobado
     *                               previamente o si el participante ya tiene un
     *                               usuario asignado
     * @throws QrCodeException       si no hay códigos QR disponibles para asignar
     */
    public void approveRegistrationByParticipantId(String participantId) throws NotFoundException {
        RegistrationStatus registrationStatus = findByParticipantId(participantId);
        Participant participant = registrationStatus.getParticipant();

        // enviamos a crearle un usuario en blanco al participante
        CompdesUser compdesUser = compdesUserService.initializeBlankParticipantUser(participant);

        // mandamos a asociar al participante con un Qr
        participant = participantService.assignQrToParticipant(participant);

        // aprobamos
        registrationStatus.approve();

        // guardamos los cambios del status del registro
        registrationStatusRepository.save(registrationStatus);

        // lanzamos el evento de que se aprobo un participante

        // TERMINAAAAAR

    }

    /**
     * Busca y devuelve el estado de registro asociado a un participante.
     * 
     * Este método consulta el repositorio utilizando el identificador del
     * participante. Si no se encuentra un estado de registro correspondiente, se
     * lanza una excepción indicando que no existe la relación.
     * 
     * @param participantId identificador del participante
     * @return el estado de registro asociado al participante
     * @throws NotFoundException si no se encuentra un estado de registro para el
     *                           participante dado
     */
    public RegistrationStatus findByParticipantId(String participantId) throws NotFoundException {
        return registrationStatusRepository.findByParticipant_Id(participantId).orElseThrow(
                () -> new NotFoundException(
                        "No se encontró un estado de registro asociado al participante con ID: " + participantId));
    }
}
