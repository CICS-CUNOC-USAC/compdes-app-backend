package com.compdes.reports.csv.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.compdes.activity.enums.ActivityType;
import com.compdes.activity.models.entities.Activity;
import com.compdes.activity.services.ActivityService;
import com.compdes.reports.csv.util.CsvExporter;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de generar el reporte en formato CSV de las actividades
 * de tipo Ponencia y Taller.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-08-02
 */
@Service
@RequiredArgsConstructor
public class PresentationsAndWorkshopsCSVReportService {
    private final ActivityService activityService;
    private final CsvExporter csvExporter;

    /**
     * Genera y exporta en memoria el reporte CSV de las actividades de tipo
     * Ponencia y Taller.
     * 
     * @return el contenido del archivo CSV como un arreglo de bytes
     */
    public byte[] exportPresentationsAndWorkshops() {
        // trae todas las actividades
        List<Activity> activities = activityService.getAllActivities();
        // filtra solo talleres y ponencias
        List<Activity> presentationsAndWorkshops = getPresentationsAndWorkShops(activities);
        // obtiene las rows del reporte
        List<String[]> lines = getReportLines(presentationsAndWorkshops);
        return csvExporter.writeAllLines(lines);
    }

    /**
     * Construye las filas del reporte CSV a partir de la lista de actividades.
     * 
     * @param activities lista de actividades filtradas
     * @return lista de arreglos de cadenas que representan las filas del CSV
     */
    private List<String[]> getReportLines(List<Activity> activities) {
        List<String[]> csvRows = new ArrayList<>();
        // Header row
        csvRows.add(new String[] { "Nombre Actividad", "Tipo de actividad" });
        // Data rows
        for (Activity activity : activities) {
            csvRows.add(new String[] { activity.getName(), activity.getType().getDisplayName() });
        }
        return csvRows;
    }

    /**
     * Filtra las actividades para obtener únicamente las de tipo Ponencia y Taller.
     * 
     * @param activities lista completa de actividades
     * @return lista de actividades cuyo tipo sea {@code PRESENTATION} o
     *         {@code WORKSHOP}
     */
    private List<Activity> getPresentationsAndWorkShops(List<Activity> activities) {
        return activities.stream()
                .filter(t -> t.getType() == ActivityType.PRESENTATION || t.getType() == ActivityType.WORKSHOP).toList();
    }
}
