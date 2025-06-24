package com.compdes.registrationStatus.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compdes.auth.users.models.entities.CompdesUser;
import com.compdes.auth.users.services.CompdesUserService;
import com.compdes.common.exceptions.DuplicateResourceException;
import com.compdes.common.exceptions.NotFoundException;
import com.compdes.common.exceptions.enums.CustomRuntimeErrorEnum;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.services.ParticipantService;
import com.compdes.registrationStatus.events.RegistrationApprovedEvent;
import com.compdes.registrationStatus.events.publishers.RegistrationEventPublisher;
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
    private final RegistrationEventPublisher registrationEventPublisher;

    public RegistrationStatusService(RegistrationStatusRepository registrationStatusRepository,
            @Lazy CompdesUserService compdesUserService, @Lazy ParticipantService participantService,
            RegistrationEventPublisher registrationEventPublisher) {
        this.registrationStatusRepository = registrationStatusRepository;
        this.compdesUserService = compdesUserService;
        this.participantService = participantService;
        this.registrationEventPublisher = registrationEventPublisher;
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
    public RegistrationStatus createRegistrationStatus(RegistrationStatus registrationStatus, Participant participant) {

        if (registrationStatus.getIsApproved() == null) {
            throw CustomRuntimeErrorEnum.REGISTRATION_STATUS_INCOMPLETE.getCustomRuntimeException();
        }

        if (registrationStatus.getIsCashPayment() == null && !participant.getIsGuest()) {
            throw CustomRuntimeErrorEnum.NO_AUTHOR_REGISTRATION_STATUS_INCOMPLETE.getCustomRuntimeException();
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
     * Actualiza el número de comprobante de un estado de registro existente.
     * 
     * Este método busca el estado de registro por su identificador y actualiza el
     * campo {@code voucherNumber}, siempre que el pago sea en efectivo.
     * Antes de realizar la actualización, valida que el nuevo número de comprobante
     * no esté ya en uso por otro participante.
     * 
     * @param id            identificador del estado de registro a actualizar
     * @param voucherNumber nuevo número de comprobante a asignar
     * @return el estado de registro actualizado
     * @throws NotFoundException          si no se encuentra un estado de registro
     *                                    con el ID proporcionado
     * @throws DuplicateResourceException si el número de comprobante ya está
     *                                    registrado por otro participante
     * @throws IllegalArgumentException   si el pago no fue en efectivo y no se
     *                                    permite asignar número de comprobante
     */
    public RegistrationStatus updateRegistrationStatus(String id, String voucherNumber) throws NotFoundException {
        RegistrationStatus registrationStatus = getRegistrationStatusById(id);// busca el registro por el id
        // asgura que no exista otro participante con el mismo numero de voucher
        if (voucherNumber != null && registrationStatusRepository.existsByVoucherNumberAndIdIsNot(voucherNumber, id)) {
            throw new DuplicateResourceException("El número de comprobante ya está en uso por otro participante.");
        }
        registrationStatus.updateVoucherNumber(voucherNumber);// invoca la logica de update
        return registrationStatusRepository.save(registrationStatus);
    }

    /**
     * Aprueba el estado de registro de un participante a partir de su
     * identificador.
     * 
     * Este método busca al participante por su ID, recupera su estado de registro y
     * delega
     * la aprobación al método correspondiente.
     * 
     * @param participantId identificador único del participante
     * @throws NotFoundException si no se encuentra el estado de registro asociado
     *                           al participante
     */
    public void approveRegistrationByParticipantId(String participantId) throws NotFoundException {
        RegistrationStatus registrationStatus = findByParticipantId(participantId);
        approveRegistrationByParticipant(registrationStatus.getParticipant());
    }

    /**
     * Aprueba el estado de registro de un participante.
     * 
     * Este método valida y actualiza el estado de registro de un participante,
     * inicializando un usuario en blanco, generando su código QR y publicando el
     * evento correspondiente.
     * 
     * <strong>Nota:</strong> El participante debe tener su estado de registro
     * (`registrationStatus`)
     * previamente inicializado o estar dentro del contexto de persistencia de
     * Spring
     * (por ejemplo, haber sido recuperado directamente desde el repositorio) para
     * que
     * la asociación pueda resolverse correctamente.
     * 
     * @param participant participante cuyo estado de registro será aprobado
     */
    public void approveRegistrationByParticipant(Participant participant) {
        RegistrationStatus registrationStatus = participant.getRegistrationStatus();

        // enviamos a crearle un usuario en blanco al participante
        CompdesUser compdesUser = compdesUserService.initializeBlankParticipantUser(participant);

        // mandamos a asociar al participante con un Qr
        participant = participantService.assignQrToParticipant(participant);

        // aprobamos
        registrationStatus.approve();

        // guardamos los cambios del status del registro
        registrationStatusRepository.save(registrationStatus);

        // lanzamos el evento de que se aprobo un participante
        registrationEventPublisher
                .publishRegistrationApproved(new RegistrationApprovedEvent(compdesUser.getId(), participant.getEmail(),
                        participant.getFullName()));
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

    /**
     * Busca un estado de registro por su identificador.
     * 
     * @param id identificador único del estado de registro
     * @return el estado de registro correspondiente al ID
     * @throws NotFoundException si no existe un estado de registro con el ID
     *                           proporcionado
     */
    public RegistrationStatus getRegistrationStatusById(String id) throws NotFoundException {
        return registrationStatusRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        "No se encontró un estado de registro con ID: " + id));
    }
}
