package com.compdes.activity.repositories;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compdes.activity.models.entities.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity,String> {
    boolean existsByClassroomIdAndInitScheduledDateLessThanEqualAndEndScheduledDateGreaterThanEqual(
            String classroomId,
            LocalDateTime initScheduledDate,
            LocalDateTime endScheduledDate
    );
}
