package com.compdes.reports.txt.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.compdes.participants.models.dto.request.ParticipantFilterDTO;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.services.ParticipantService;
import com.compdes.reports.txt.utils.ParticipantEmailTxtExporter;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de generar el reporte de asistencia de participantes del
 * CUNOC.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-08-02
 */
@Service
@RequiredArgsConstructor
public class CunocAttendanceReportService {

        private final ParticipantService participantService;

        private final ParticipantEmailTxtExporter exporter;

        /**
         * Genera un reporte de correos electrónicos de participantes aprobados del
         * CUNOC.
         * 
         * Este método filtra los participantes cuyo campo de institución educativa
         * coincida
         * con "Universidad de San Carlos de Guatemala - Centro Universitario de
         * Occidente (CUNOC - USAC)"
         * y que tengan el estado de aprobación en verdadero.
         * 
         * @return arreglo de bytes que representa el contenido del archivo de texto
         *         generado
         */
        public byte[] generateReport() {
                // busca solamente los participantes aprobados y del cunoc
                List<Participant> approvedParticipants = participantService.getAllParticipants(
                                new ParticipantFilterDTO(null, null, null, null,
                                                "Universidad de San Carlos de Guatemala - Centro Universitario de Occidente (CUNOC - USAC)",
                                                null,
                                                null, null, null, null,
                                                null, true, null));

                // Escribe los correos en un archivo de texto (en memoria)
                return exporter.writeEmailsToTxt(approvedParticipants, "Asitente CUNOC");
        }

}
