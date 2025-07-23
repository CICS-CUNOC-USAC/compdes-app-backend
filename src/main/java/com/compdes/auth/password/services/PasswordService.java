package com.compdes.auth.password.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compdes.auth.password.models.dto.request.ChangeUserDTO;
import com.compdes.common.exceptions.NotFoundException;
import com.compdes.common.exceptions.enums.ErrorCodeMessageEnum;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.services.ParticipantService;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de la gestión de contraseñas de usuarios asociados a
 * participantes.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-07-22
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PasswordService {

    private final ParticipantService participantService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Cambia la contraseña del usuario asociado a un participante.
     * 
     * @param passwordDTO   DTO que contiene la nueva contraseña a establecer
     * @param participantId ID del participante cuyo usuario se desea actualizar
     * 
     * @throws NotFoundException     si no se encuentra el participante con el ID
     *                               dado
     * @throws IllegalStateException si el participante no tiene un usuario asociado
     */
    public void resetCompdesUser(ChangeUserDTO passwordDTO, String participantId) throws NotFoundException {
        // trae al participante por su id
        Participant participant = participantService.getParticipantById(participantId);

        // evalua que el participante tenga un usuario creado
        if (participant.getCompdesUser() == null) {
            throw new IllegalStateException(ErrorCodeMessageEnum.PARTICIPANT_WITHOUT_USER.getMessage());
        }

        // encripta la nueva password
        String encodedPassword = passwordEncoder.encode(passwordDTO.getNewPassword());
        // setea la password por medio del participante
        participant.getCompdesUser().setPassword(encodedPassword);
        // resetea el nombre de usuario del participante
        participant.getCompdesUser().setUsername(participant.getEmail());
        participantService.saveParticipant(participant);

    }
}
