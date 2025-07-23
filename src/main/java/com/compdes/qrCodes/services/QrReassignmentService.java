package com.compdes.qrCodes.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.repositories.ParticipantRepository;
import com.compdes.qrCodes.models.entities.QrCode;
import com.compdes.qrCodes.repositories.QrCodeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class QrReassignmentService {

    private final ParticipantRepository participantRepository;
    private final QrCodeRepository qrCodeRepository;

    public void reassignQrsToApprovedParticipants() {
        // 1. Obtener participantes aprobados
        List<Participant> participants = participantRepository
                .findByRegistrationStatus_IsApprovedOrderByCreatedAtAsc(true);

        // 2. Obtener los códigos QR ordenados por número ascendente
        List<QrCode> qrCodes = qrCodeRepository.findAllByOrderByNumberCodeAsc();

        // 3. Validar que hay suficientes códigos
        if (qrCodes.size() < participants.size()) {
            throw new IllegalStateException("No hay suficientes códigos QR disponibles para reasignar.");
        }

        participants.forEach(participant -> participant.setQrCodeWithoutExeption(null));
        // 4. Desvincular los QR anteriores (opcional)
        qrCodes.forEach(qr -> qr.setParticipantWithoutExeption(null));

        participantRepository.saveAll(participants);
        qrCodeRepository.saveAll(qrCodes);

        // 5. Asignar los nuevos QRs
        for (int i = 0; i < participants.size(); i++) {
            Participant participant = participants.get(i);
            QrCode qrCode = qrCodes.get(i);
            participant.setQrCodeWithoutExeption(qrCode);
            qrCode.setParticipantWithoutExeption(participant); // esto validará si ya estaba asignado
        }

        // 6. Guardar cambios
        participantRepository.saveAll(participants);
        qrCodeRepository.saveAll(qrCodes);
    }
}
