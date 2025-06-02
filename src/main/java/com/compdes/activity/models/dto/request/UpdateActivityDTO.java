package com.compdes.activity.models.dto.request;

import java.time.LocalDateTime;

import com.compdes.activity.enums.ActivityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateActivityDTO {
    @NotBlank(message = "Se debe de proporcionar un nombre para la actividad")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String name;
    @NotBlank(message = "Se debe de proporcionar una descripción para la actividad")
    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    private String description;
    @NotNull(message = "Se debe de proporcionar el tipo de actividad")
    private ActivityType type;
    @NotNull(message = "Se debe de proporcionar la fecha y hora programada para la actividad")
    private LocalDateTime scheduledDate; // ISO-8601 format (e.g., "2023-10-01T10:00:00")
    @NotBlank(message = "Se debe de porporcionar el aula para la actividad")
    private String classroomId;
}
