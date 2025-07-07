package com.compdes.attendance.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CUAttendanceDTO {
    @NotBlank(message = "Se debe proporcionar el código QR del participante")
    private String qrCode;
    @NotBlank(message = "Se debe proporcionar el ID de la actividad")
    private String activityId;
}
