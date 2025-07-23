package com.compdes.attendance.models.dto.response;

import java.time.Instant;
import java.time.LocalDateTime;

import com.compdes.activity.models.dto.response.ActivityDTO;
import com.compdes.participants.models.dto.response.AdminParticipantProfileDTO;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class AttendaceDTO {
    private String id;
    private AdminParticipantProfileDTO participant;
    private ActivityDTO activity;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Instant updatedAt;
    private Instant createdAt;
}
