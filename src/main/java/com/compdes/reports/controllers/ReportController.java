package com.compdes.reports.controllers;

import java.util.List;

import com.compdes.reports.models.dto.response.ActivityAttendanceReportDTO;
import com.compdes.reports.services.ActivityAttendanceReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.compdes.reports.models.dto.response.UniversityAttendanceReportDTO;
import com.compdes.reports.services.ApprovedParticipantsByRoleEmailReportService;
import com.compdes.reports.services.ApprovedParticipantsEmailReportService;
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
    private final ApprovedParticipantsEmailReportService approvedParticipantsEmailReportService;
    private final ApprovedParticipantsByRoleEmailReportService approvedParticipantsByRoleEmailReportService;
    private final ActivityAttendanceReportService activityAttendanceReportService;

    @GetMapping("/approved-participants-by-role-email/{isAuthor}")
    public ResponseEntity<byte[]> getApprovedParticipantsByRoleEmailReport(
            @PathVariable Boolean isAuthor) {
        byte[] fileContent = approvedParticipantsByRoleEmailReportService.generateReport(isAuthor);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=approved-emails.txt")
                .contentType(MediaType.TEXT_PLAIN)
                .body(fileContent);
    }

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


    /**
     * Retorna el reporte de asistencia de participantes agrupado por universidad.
     *
     */
    @Operation(summary = "Reporte de asistencia por actividad",
            description = "Genera un reporte agrupado por actividad, incluyendo la cantidad total de participantes registrados, Retorna los perfiles completos de los participantes por actividad. Disponible para `ADMIN`",
            security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(responseCode = "200", description = "Reporte generado correctamente"),
            @ApiResponse(responseCode = "500", description = "Ocurrió un error inesperado al generar el reporte")
    })
    @GetMapping("/attendance-report-by-activity")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<ActivityAttendanceReportDTO> getAttendanceReportByActivity() {
        return activityAttendanceReportService.getAttendanceReportByActivity();
    }



    /**
     * Genera un archivo con los correos electrónicos de participantes aprobados,
     * agrupados por universidad.
     * 
     * Este método es accesible únicamente para usuarios con rol ADMIN y retorna un
     * archivo de texto plano descargable con los correos electrónicos aprobados por
     * universidad.
     * 
     * @return archivo .txt con los correos electrónicos de participantes aprobados
     */
    @Operation(summary = "Reporte de correos aprobados por universidad", description = "Genera un archivo .txt con los correos electrónicos de los participantes cuya inscripción ha sido aprobada, agrupados por universidad. Disponible solo para usuarios con rol ADMIN.", security = @SecurityRequirement(name = "bearerAuth"), responses = {
            @ApiResponse(responseCode = "200", description = "Archivo generado correctamente"),
            @ApiResponse(responseCode = "500", description = "Ocurrió un error inesperado al generar el archivo")
    })
    @GetMapping("/get-approved-participants-email")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> getApprovedParticipantsEmail() {
        byte[] fileContent = approvedParticipantsEmailReportService.generateReport();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=approved-emails.txt")
                .contentType(MediaType.TEXT_PLAIN)
                .body(fileContent);
    }

}
