package com.compdes.activity.models.dto.response;

import com.compdes.activity.enums.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityDTO {
    private String id;
    private String name;
    private String description;
    private ActivityType type;
    private LocalDateTime scheduledDate;
}
