package com.compdes.reports.models.dto.response;

import java.util.List;

import com.compdes.participants.models.dto.response.AdminParticipantProfileDTO;

import lombok.Value;

/**
 * Representa un reporte de asistencia de participantes agrupados por
 * universidad.
 * 
 * <p>
 * Este DTO se utiliza para resumir la cantidad total de participantes
 * registrados por universidad y proporcionar la lista completa de sus perfiles.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-07-09
 */
@Value
public class UniversityAttendanceReportDTO {

    String university;
    Integer totalRegistered;
    Long totalApproved;
    Long totalPending;
    List<AdminParticipantProfileDTO> participants;
}
