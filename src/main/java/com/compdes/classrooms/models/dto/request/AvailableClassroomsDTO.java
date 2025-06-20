package com.compdes.classrooms.models.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AvailableClassroomsDTO {
    @NotNull(message = "La fecha y hora de inicio no puede ser nula")
    LocalDateTime initScheduledDate;
    @NotNull(message = "La fecha y hora de fin no puede ser nula")
    LocalDateTime endScheduledDate;
}
