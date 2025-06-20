package com.compdes.activity.models.dto.response;

import com.compdes.activity.enums.ActivityType;
import com.compdes.classrooms.models.dto.response.ResponseClassroomDTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityDTO {
    private String id;
    private String name;
    private String description;
    private ActivityType type;
    private LocalDateTime initScheduledDate;
    private LocalDateTime endScheduledDate;
    private ResponseClassroomDTO classroom;
}
