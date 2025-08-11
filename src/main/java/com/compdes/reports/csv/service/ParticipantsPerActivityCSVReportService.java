package com.compdes.reports.csv.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.compdes.attendance.models.report.ActivityAttendanceAggregate;
import com.compdes.attendance.repositories.AttendanceRepository;
import com.compdes.reports.csv.util.CsvExporter;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de generar el reporte CSV con el total de participantes
 * por actividad.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-08-02
 */
@Service
@RequiredArgsConstructor
public class ParticipantsPerActivityCSVReportService {

    private final AttendanceRepository attendanceRepository;
    private final CsvExporter csvExporter;

    /**
     * Genera y retorna el archivo CSV con el conteo de participantes por actividad.
     * 
     * 
     * @return contenido del archivo CSV como arreglo de bytes
     */
    public byte[] exportParticipantsPerActivity() {
        List<ActivityAttendanceAggregate> aggregates = attendanceRepository.countDistinctParticipantsByActivity();
        List<String[]> rows = buildCsvRows(aggregates);
        return csvExporter.writeAllLines(rows);
    }

    /**
     * Construye las filas del CSV (cabecera + datos) a partir de los agregados de
     * asistencia.
     * 
     * @param aggregates lista de agregados por actividad
     * @return filas del CSV
     */
    private List<String[]> buildCsvRows(List<ActivityAttendanceAggregate> aggregates) {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[] { "Nombre Actividad", "Total Participantes" });
        for (ActivityAttendanceAggregate aggregate : aggregates) {
            rows.add(new String[] {
                    aggregate.getActivityName(),
                    aggregate.getTotalParticipants().toString()
            });
        }
        return rows;
    }

}
