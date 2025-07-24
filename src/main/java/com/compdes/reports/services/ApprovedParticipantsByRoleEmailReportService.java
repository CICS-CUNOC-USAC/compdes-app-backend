package com.compdes.reports.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.compdes.common.exceptions.CustomRuntimeException;
import com.compdes.participants.models.dto.request.ParticipantFilterDTO;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.services.ParticipantService;
import com.compdes.reports.utils.ParticipantEmailTxtExporter;

import lombok.RequiredArgsConstructor;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-07-24
 */
@Service
@RequiredArgsConstructor
public class ApprovedParticipantsByRoleEmailReportService {
    private final ParticipantService participantService;
    private final ParticipantEmailTxtExporter exporter;

    /**
     * Genera un reporte en formato `.txt` con los correos electrónicos de los
     * participantes aprobados.
     * 
     * @return un arreglo de bytes que contiene el contenido del archivo `.txt` con
     *         los correos aprobados.
     * @throws CustomRuntimeException si ocurre un error durante la generación del
     *                                archivo.
     */
    public byte[] generateReport(Boolean isAuthor) {
        // busca solamente los participantes aprovados, sehgun sean autores o no
        List<Participant> approvedParticipants = participantService.getAllParticipants(
                new ParticipantFilterDTO(null, null, null, null, null,
                        null, isAuthor, null, null,
                        null, null, true, null));

        String participantType = isAuthor ? "Autor" : "Participante";

        // Escribe en memmoria los correos en un archivo de texto
        return exporter.writeEmailsToTxt(approvedParticipants, participantType);
    }
}
