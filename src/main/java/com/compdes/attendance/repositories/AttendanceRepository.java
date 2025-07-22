package com.compdes.attendance.repositories;

import com.compdes.attendance.models.entities.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,String> {
    Optional<Attendance> findByParticipantIdAndActivityId(String participantId, String activityId);
    boolean existsByParticipantIdAndActivityId(String participantId, String activityId);
    List<Attendance> findAllByActivityId(String activityId);
    List<Attendance> findAllByParticipantId(String participantId);
}
