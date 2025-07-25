package com.compdes.reports.models.dto.response;

import com.compdes.activity.models.dto.response.ActivityDTO;
import com.compdes.attendance.models.dto.response.AttendaceDTO;
import com.compdes.participants.models.dto.response.AdminParticipantProfileDTO;
import lombok.Value;

import java.util.List;

/**
 * Representa un reporte de asistencia de participantes agrupados por
 * actividad
 *
 * <p>
 * Este DTO se utiliza para resumir la cantidad total de participantes
 * registrados por universidad y proporcionar la lista completa de sus perfiles.
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-07-24
 */
@Value
public class ActivityAttendanceReportDTO {
    List<AdminParticipantProfileDTO> participants;
    ActivityDTO activity;
    Integer totalRegistered;
}
