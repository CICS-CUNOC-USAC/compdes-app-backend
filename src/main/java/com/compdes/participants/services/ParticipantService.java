package com.compdes.participants.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compdes.common.exceptions.DuplicateResourceException;
import com.compdes.participants.mappers.ParticipantMapper;
import com.compdes.participants.models.dto.request.CreateParticipantDTO;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.repositories.ParticipantRepository;
import com.compdes.paymentProofs.models.entities.PaymentProof;
import com.compdes.paymentProofs.services.PaymentProofService;
import com.compdes.registrationStatus.models.entities.RegistrationStatus;
import com.compdes.registrationStatus.services.RegistrationStatusService;

import lombok.RequiredArgsConstructor;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-30
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantMapper participantMapper;
    private final ParticipantRepository participantRepository;
    private final PaymentProofService paymentProofService;
    private final RegistrationStatusService registrationStatusService;

    public Participant createNonAuthorParticipant(CreateParticipantDTO createParticipantDTO) {

        // mandar a guardar al participante
        Participant savedParticipant = saveGenericParticipantOlineMethod(createParticipantDTO, false, false);

        // mandamos a crear la prueba de pago y lo asignamos
        PaymentProof paymentProof = paymentProofService.createPaymentProof(createParticipantDTO.getPaymentProof(),
                savedParticipant);
        savedParticipant.setPaymentProof(paymentProof);

        // le gurardamos al participante su pruba de pago y guardamos los cambios
        savedParticipant.setPaymentProof(paymentProof);
        savedParticipant = participantRepository.save(savedParticipant);

        return savedParticipant;
    }

    public Participant createAuthorParticipant(CreateParticipantDTO createParticipantDTO) {

        return null;
    }

    private Participant saveGenericParticipantOlineMethod(CreateParticipantDTO createParticipantDTO, Boolean isAuthor,
            Boolean isCashPayment) throws DuplicateResourceException {

        Participant participant = participantMapper.createParticipantDtoToParticipant(createParticipantDTO);
        participant.setIsAuthor(isAuthor);//le seteamos si es un autor o no

        // verificar que no exista otro participante con el mismo email
        if (participantRepository.existsByEmail(participant.getEmail())) {
            throw new DuplicateResourceException(
                    "No se puede completar el registro: el correo ingresado ya está asociado a otro participante.");
        }

        // verificar que no exista otro participante con el mismo doc de identificacion
        if (participantRepository.existsByIdentificationDocument(participant.getIdentificationDocument())) {
            throw new DuplicateResourceException(
                    "No se puede completar el registro: el documento de identificación ya está asociado a otro participante.");

        }

        //guardamos el participante para obtener un id y podr asociar una constancia de pago
        participant = participantRepository.save(participant);

        // le creamos un estado de registro y lo guardamos
        RegistrationStatus registrationStatus = new RegistrationStatus(participant, false, isCashPayment);
        registrationStatus = registrationStatusService
                .createRegistrationStatus(registrationStatus, participant);

        // le seteamos al participante su estado de registro
        participant.setRegistrationStatus(registrationStatus);
        participant = participantRepository.save(participant);

        return participant;
    }

}
