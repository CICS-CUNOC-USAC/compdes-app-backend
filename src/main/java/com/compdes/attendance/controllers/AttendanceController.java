package com.compdes.attendance.controllers;

import com.compdes.attendance.mappers.AttendanceMapper;
import com.compdes.attendance.models.dto.request.CUAttendanceDTO;
import com.compdes.attendance.models.dto.response.AttendaceDTO;
import com.compdes.attendance.services.AttendanceService;
import com.compdes.common.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attendances")
@AllArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final AttendanceMapper attendanceMapper;

    @Operation(summary = "Obtiene todas las asistencias", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de asistencias obtenida exitosamente"),
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AttendaceDTO> getAllAttendances() {
        return attendanceMapper.toAttendaceDTOList(attendanceService.getAllAttendances());
    }

    @Operation(summary = "Obtiene las asistencias de una actividad por su ID", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de asistencias obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Actividad no encontrada"),
    })
    @GetMapping("/activity/{activityId}/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AttendaceDTO> getAllAttendancesByActivityId(String activityId) throws NotFoundException {
        return attendanceMapper.toAttendaceDTOList(attendanceService.getAttendancesByActivityId(activityId));
    }

    @Operation(summary = "Obtiene las asistencias de un participante por su ID", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de asistencias obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Participante no encontrado"),
    })
    @GetMapping("/participant/{participantId}/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AttendaceDTO> getAllAttendancesByParticipantId(String participantId) throws NotFoundException {
        return attendanceMapper.toAttendaceDTOList(attendanceService.getAttendancesByParticipantId(participantId));
    }

    @Operation(summary = "Obtiene una asistencia por medio del ID del participante y el ID de la actividad", responses = {
            @ApiResponse(responseCode = "200", description = "Asistencia obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Asistencia no encontrada"),
    })
    @GetMapping("/activity/{activityId}/participant/{participantId}")
    @ResponseStatus(HttpStatus.OK)
    public AttendaceDTO getAttendanceByParticipantIdAndActivityId(String participantId, String activityId) throws NotFoundException {
        return attendanceMapper.toAttendaceDTO(attendanceService.getAttendanceByParticipantIdAndActivityId(participantId, activityId));
    }

    @Operation(summary = "Crea una nueva asistencia", responses = {
            @ApiResponse(responseCode = "201", description = "Asistencia creada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Participante o actividad no encontrada"),
            @ApiResponse(responseCode = "409", description = "Participante ya tiene una asistencia registrada para la actividad"),
    }
    )
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public AttendaceDTO createAttendance(@RequestBody CUAttendanceDTO createAttendanceDTO) throws NotFoundException {
        return attendanceMapper.toAttendaceDTO(attendanceService.createAttendance(createAttendanceDTO));
    }

    @Operation(summary = "Cierra la asistencia a una actividad", responses = {
            @ApiResponse(responseCode = "200", description = "Asistencia actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Asistencia no encontrada"),
    })
    @PatchMapping("/exit")
    @ResponseStatus(HttpStatus.OK)
    public AttendaceDTO exitAttendance(@RequestBody CUAttendanceDTO exitAttendanceDTO) throws NotFoundException {
        return attendanceMapper.toAttendaceDTO(attendanceService.markAttendanceExit(exitAttendanceDTO));
    }

}
