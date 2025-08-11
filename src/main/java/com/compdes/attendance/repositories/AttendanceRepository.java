package com.compdes.attendance.repositories;

import com.compdes.attendance.models.entities.Attendance;
import com.compdes.attendance.models.report.ActivityAttendanceAggregate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, String> {
    Optional<Attendance> findByParticipantIdAndActivityId(String participantId, String activityId);

    boolean existsByParticipantIdAndActivityId(String participantId, String activityId);

    List<Attendance> findAllByActivityId(String activityId);

    List<Attendance> findAllByParticipantId(String participantId);

    @Query("""
            select
                a.name as activityName,
                count(distinct att.participant.id) as totalParticipants
            from Activity a
            left join Attendance att on att.activity.id = a.id
            group by a.id
            """)
    List<ActivityAttendanceAggregate> countDistinctParticipantsByActivity();
}
