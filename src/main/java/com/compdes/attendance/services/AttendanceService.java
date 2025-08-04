package com.compdes.attendance.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.compdes.activity.models.entities.Activity;
import com.compdes.activity.services.ActivityService;
import com.compdes.attendance.mappers.AttendanceMapper;
import com.compdes.attendance.models.dto.request.CUAttendanceDTO;
import com.compdes.attendance.models.entities.Attendance;
import com.compdes.attendance.repositories.AttendanceRepository;
import com.compdes.common.exceptions.NotFoundException;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.services.ParticipantService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final ParticipantService participantService;
    private final ActivityService activityService;
    private final AttendanceMapper attendanceMapper;

    /**
     * Obtiene la asistencia de un participante para una actividad específica.
     *
     * @param participantId ID del participante.
     * @param activityId ID de la actividad.
     * @return Attendance
     * @throws NotFoundException si no se encuentra la asistencia.
     */
    public Attendance getAttendanceByParticipantIdAndActivityId(String participantId, String activityId) throws NotFoundException {
        return attendanceRepository.findByParticipantIdAndActivityId(participantId, activityId).orElseThrow(() ->
            new NotFoundException("No se encontró la asistencia del participante con ID: " + participantId +
                                  " para la actividad con ID: " + activityId));
    }

    /**
     * Obtiene la asistencia por ID.
     *
     * @param attendanceId ID de la asistencia.
     * @return Attendance
     * @throws NotFoundException si no se encuentra la asistencia.
     */
    public Attendance getAttendanceById(String attendanceId) throws NotFoundException {
        return attendanceRepository.findById(attendanceId).orElseThrow(() ->
            new NotFoundException("No se encontró la asistencia con ID: " + attendanceId));
    }

    /**
     * Crea una nueva asistencia para un participante en una actividad.
     *
     * @param createAttendanceDTO DTO con los datos necesarios para crear la asistencia.
     * @return Attendance
     * @throws NotFoundException si no se encuentra el participante o la actividad.
     * @throws IllegalStateException si el participante ya tiene una asistencia registrada para la actividad.
     */
    public Attendance createAttendance(CUAttendanceDTO createAttendanceDTO) throws NotFoundException {
        Participant participant = participantService.getParticipantByQrCodeId(createAttendanceDTO.getQrCode());
        Activity activity = activityService.getActivityById(createAttendanceDTO.getActivityId());
        if (attendanceRepository.existsByParticipantIdAndActivityId(
                participant.getId(),
                activity.getId()
        )) {
            throw new IllegalStateException("El participante ya tiene una asistencia registrada para esta actividad.");
        }
        Attendance attendance = new Attendance(
                activity,
                participant,
                LocalDateTime.now(),
                null
        );
        return attendanceRepository.save(attendance);
    }

    /**
     * Marca la salida de un participante de una actividad.
     *
     * @param CUAttendanceDTO DTO con los datos necesarios para marcar la salida.
     * @return Attendance
     * @throws NotFoundException si no se encuentra el participante o la actividad.
     * @throws IllegalStateException si el participante no tiene una asistencia registrada para la actividad.
     */
    public Attendance markAttendanceExit(CUAttendanceDTO CUAttendanceDTO) throws NotFoundException {
        Participant participant = this.participantService.getParticipantByQrCodeId(CUAttendanceDTO.getQrCode());
        Activity activity = this.activityService.getActivityById(CUAttendanceDTO.getActivityId());
        if (!attendanceRepository.existsByParticipantIdAndActivityId(
                participant.getId(),
                activity.getId()
        )) {
            throw new IllegalStateException("El participante no tiene una asistencia registrada para esta actividad.");
        }
        Attendance attendance = this.getAttendanceByParticipantIdAndActivityId(
                participant.getId(),
                activity.getId()
        );
        attendance.setExitTime(LocalDateTime.now());
        return attendanceRepository.save(attendance);
    }

    /**
     * Obtiene todas las asistencias registradas.
     * @return List<Attendance>
     */
    public List<Attendance> getAllAttendances() {
        return attendanceRepository.findAll();
    }

    /**
     * Obtiene todas las asistencias de una actividad específica.
     *
     * @param activityId ID de la actividad.
     * @return List<Attendance>
     * @throws NotFoundException si no se encuentra la actividad.
     */
    public List<Attendance> getAttendancesByActivityId(String activityId) throws NotFoundException {
        Activity activity = activityService.getActivityById(activityId);
        return attendanceRepository.findAllByActivityId(activity.getId());
    }

    /**
     * Obtiene todas las asistencias de un participante específico.
     * @param qrCode Código QR del participante.
     * @return List<Attendance>
     * @throws NotFoundException si no se encuentra el participante.
     */
    public List<Attendance> getAttendancesByParticipantId(String qrCode) throws NotFoundException {
        Participant participant = participantService.getParticipantByQrCodeId(qrCode);
        return attendanceRepository.findAllByParticipantId(participant.getId());
    }
}
