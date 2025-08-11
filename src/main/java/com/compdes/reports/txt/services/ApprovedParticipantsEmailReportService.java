package com.compdes.reports.txt.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.compdes.common.exceptions.CustomRuntimeException;
import com.compdes.participants.models.dto.request.ParticipantFilterDTO;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.services.ParticipantService;
import com.compdes.reports.txt.utils.ParticipantEmailTxtExporter;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de generar un reporte en formato texto (.txt)
 * que contiene los correos electrónicos de los participantes aprobados.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-07-22
 */
@Service
@RequiredArgsConstructor
public class ApprovedParticipantsEmailReportService {

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
    public byte[] generateReport() {
        // busca solamente los participantes aprovados
        List<Participant> approvedParticipants = participantService.getAllParticipants(
                new ParticipantFilterDTO(null, null, null, null, null, null, null, null, null, null, null, true, null));

        // Escribe los correos en un archivo de texto (en memoria)
        return exporter.writeEmailsToTxt(approvedParticipants, "Parcticipante");
    }
}
