package com.compdes.reports.txt.services;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.compdes.activity.mappers.ActivityMapper;
import com.compdes.activity.models.dto.response.ActivityDTO;
import com.compdes.activity.models.entities.Activity;
import com.compdes.attendance.models.entities.Attendance;
import com.compdes.attendance.services.AttendanceService;
import com.compdes.participants.mappers.ParticipantMapper;
import com.compdes.participants.models.dto.response.AdminParticipantProfileDTO;
import com.compdes.reports.txt.models.dto.response.ActivityAttendanceReportDTO;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de generar reportes estadísticos de asistencia de
 * participantes agrupados por actividad.
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-07-24
 */
@Service
@RequiredArgsConstructor
public class ActivityAttendanceReportService {

    private final AttendanceService attendanceService;
    private final ActivityMapper activityMapper;
    private final ParticipantMapper participantMapper;

    public List<ActivityAttendanceReportDTO> getAttendanceReportByActivity(){
        // Obtiene y agrupa todas las asistencias por actividad
        Map<Activity, List<Attendance>> groupedByActivity = getAssistantGroupedByActivity();

        return groupedByActivity.entrySet().stream()
                .map(entry -> buildActivityReport(entry.getKey(), entry.getValue()))
                .toList();
    }

    /**
     * Recupera todas las asistencias y las agrupa por actividad
     *
     */
    private Map<Activity, List<Attendance>> getAssistantGroupedByActivity() {
        List<Attendance> allAttendances = attendanceService.getAllAttendances();

        return allAttendances.stream()
                .collect(Collectors.groupingBy(Attendance::getActivity));
    }


    /**
     * Construye un reporte detallado para una actividad específica, incluyendo
     * la cantidad total de participantes, cuántos han sido aprobados y cuántos
     * están pendientes.
     */
    private ActivityAttendanceReportDTO buildActivityReport(Activity activity, List<Attendance> attendances) {

        List<AdminParticipantProfileDTO> participantDTOs = attendances.stream()
                .map(Attendance::getParticipant)
                .filter(Objects::nonNull)
                .map(participantMapper::participantToPrivateParticipantInfoDto)
                .toList();

        ActivityDTO activityDTO = activityMapper.toActivityDTO(activity);

        return new ActivityAttendanceReportDTO(
                participantDTOs, activityDTO, participantDTOs.size()
        );
    }




}
