package com.compdes.reports.csv.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.compdes.participants.models.dto.request.ParticipantFilterDTO;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.services.ParticipantService;
import com.compdes.reports.csv.util.CsvExporter;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de generar el reporte en formato CSV de la distribución de
 * participantes por institución.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-08-02
 */
@Service
@RequiredArgsConstructor
public class ParticipantsByInstitutionCSVReportService {

    private final CsvExporter csvReportFileExporter;
    private final ParticipantService participantService;

    /**
     * Genera y exporta en memoria el reporte CSV de distribución de participantes
     * por institución.
     * 
     * @return el contenido del archivo CSV como un arreglo de bytes, listo para ser
     *         enviado en la respuesta HTTP
     */
    public byte[] exportParticipantsByInstitutionCSVReport() {
        List<Participant> approved = fetchApprovedParticipants();
        Map<String, Long> counts = groupParticipantsByInstitution(approved);
        // exporta el resultado
        return csvReportFileExporter.writeAllLines(getReportLines(counts));
    }

    /**
     * Obtiene todos los participantes con estado aprobado.
     * 
     * @return lista de participantes aprobados
     */
    private List<Participant> fetchApprovedParticipants() {
        return participantService.getAllParticipants(
                new ParticipantFilterDTO(
                        null, null, null, null, null, null, null, null, null, null, null,
                        true,
                        null));
    }

    /**
     * Agrupa a los participantes por institución y cuenta la cantidad por grupo.
     * 
     * @param participants lista de participantes
     * @return mapa institución - total
     */
    private Map<String, Long> groupParticipantsByInstitution(List<Participant> participants) {
        // agrupa por organizacion
        return participants.stream()
                .collect(
                        Collectors.groupingBy(t -> t.getOrganisation(), Collectors.counting()));
    }

    /**
     * Construye las filas del reporte CSV de distribución de participantes por
     * institución.
     * 
     * Cada fila siguiente contiene el nombre de la institución y la cantidad total
     * de participantes asociados a ella.
     * 
     * @param participantsByInstitution mapa donde la clave es el nombre de la
     *                                  institución
     *                                  y el valor es la cantidad de participantes
     * @return lista de arreglos de cadenas, donde cada arreglo representa una fila
     *         del CSV
     */
    private List<String[]> getReportLines(Map<String, Long> participantsByInstitution) {
        List<String[]> csvRows = new ArrayList<>();
        // Header row
        csvRows.add(new String[] { "Institucion", "Total participantes" });
        // Data rows
        for (Map.Entry<String, Long> entry : participantsByInstitution.entrySet()) {
            csvRows.add(new String[] { entry.getKey(), String.valueOf(entry.getValue()) });
        }
        return csvRows;
    }

}
