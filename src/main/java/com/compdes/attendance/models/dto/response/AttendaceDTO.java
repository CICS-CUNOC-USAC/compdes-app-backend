package com.compdes.attendance.models.dto.response;

import com.compdes.activity.models.dto.response.ActivityDTO;
import com.compdes.participants.models.dto.response.BaseParticipantInfoDTO;

import java.time.Instant;
import java.time.LocalDateTime;

public class AttendaceDTO {
    private String id;
    private BaseParticipantInfoDTO participant;
    private ActivityDTO activity;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Instant updatedAt;
    private Instant createdAt;
}
