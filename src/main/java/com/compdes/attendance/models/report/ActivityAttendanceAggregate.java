package com.compdes.attendance.models.report;

/**
 * Proyección de conteo de participantes por actividad.
 */
public interface ActivityAttendanceAggregate {

    String getActivityName();

    Long getTotalParticipants();
}
