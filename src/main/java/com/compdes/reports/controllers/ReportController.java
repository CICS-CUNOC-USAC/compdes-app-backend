package com.compdes.reports.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.compdes.reports.models.dto.response.UniversityAttendanceReportDTO;
import com.compdes.reports.services.UniversityAttendanceReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

/**
 * Controlador encargado de exponer los endpoints relacionados con reportes del
 * sistema.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-07-09
 */
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final UniversityAttendanceReportService universityAttendanceReportService;

    /**
     * Retorna el reporte de asistencia de participantes agrupado por universidad.
     * 
     * @return lista de objetos {@link UniversityAttendanceReportDTO} representando
     *         las estadísticas de inscripción por universidad
     */
    @Operation(summary = "Reporte de asistencia por universidad", description = "Genera un reporte agrupado por universidad, incluyendo la cantidad total de participantes registrados, el número de inscripciones aprobadas y las pendientes. Retorna los perfiles completos de los participantes por universidad. Disponible para `ADMIN`", security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(responseCode = "200", description = "Reporte generado correctamente"),
            @ApiResponse(responseCode = "500", description = "Ocurrió un error inesperado al generar el reporte")
    })
    @GetMapping("/attendance-report-by-university")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<UniversityAttendanceReportDTO> getAttendanceReportByUniversity() {
        return universityAttendanceReportService.getAttendanceReportByUniversity();
    }

}
