package com.compdes.reports.csv.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.compdes.common.exceptions.CustomRuntimeException;
import com.compdes.participants.models.dto.request.ParticipantFilterDTO;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.services.ParticipantService;
import com.compdes.reports.csv.util.CsvExporter;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de generar el reporte en formato CSV del total de
 * participantes aprobados.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-08-02
 */
@Service
@RequiredArgsConstructor
public class TotalParticipantsCSVReportService {

    private final CsvExporter csvExporter;
    private final ParticipantService participantService;

    /**
     * Genera el reporte del total de participantes aprobados en formato CSV.
     * 
     * @return el contenido del reporte en formato CSV como un arreglo de bytes
     * @throws CustomRuntimeException si ocurre un error durante la generación del
     *                                CSV
     */
    public byte[] exportTotalParticipantsCSVReport() {

        // busca solamente los participantes aprovados
        List<Participant> approvedParticipants = participantService.getAllParticipants(
                new ParticipantFilterDTO(null, null, null, null, null, null, null, null, null, null, null, true, null));

        List<String[]> lines = getReportLines(approvedParticipants);
        return csvExporter.writeAllLines(lines);
    }

    /**
     * Construye las líneas del reporte CSV a partir de la lista de participantes.
     * 
     * @param participants lista de participantes aprobados
     * @return lista de arreglos de cadenas que representan las líneas del archivo
     *         CSV
     */
    private List<String[]> getReportLines(List<Participant> participants) {
        String[] columnsTitles = { "Total Participantes" };
        String[] total = { Integer.toString(participants.size()) };
        return List.of(columnsTitles, total);
    }
}
