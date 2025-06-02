package com.compdes.activity.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.compdes.activity.mappers.ActivityMapper;
import com.compdes.activity.models.dto.request.CreateActivityDTO;
import com.compdes.activity.models.dto.request.UpdateActivityDTO;
import com.compdes.activity.models.dto.response.ActivityDTO;
import com.compdes.activity.services.ActivityService;
import com.compdes.common.exceptions.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/activities")
@AllArgsConstructor
public class ActivityController {
    private final ActivityService activityService;
    private final ActivityMapper activityMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ActivityDTO> getAllActivities() {
        return activityMapper.toActivityDTOList(activityService.getAllActivities());
    }


    @Operation(summary = "Crear nueva actividad", responses = {
        @ApiResponse(responseCode = "201", description = "Actividad creada exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado: requiere rol ADMIN"),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void createActivity(@RequestBody @Valid CreateActivityDTO createActivityDTO) {
        activityService.createActivity(createActivityDTO);
    }

    @Operation(summary = "Obtiene una actividad por medio de su ID", responses = {
        @ApiResponse(responseCode = "204", description = "Actividad eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Actividad no encontrada"),
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ActivityDTO getActivityById(@PathVariable String id) throws NotFoundException {
        return activityMapper.toActivityDTO(activityService.getActivityById(id));
    }

    @Operation(summary = "Actualiza una actividad por medio de su ID", responses = {
        @ApiResponse(responseCode = "200", description = "Actividad actualizada exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado: requiere rol ADMIN"),
        @ApiResponse(responseCode = "404", description = "Actividad no encontrada"),
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ActivityDTO updateActivity(@PathVariable String id,
            @RequestBody @Valid UpdateActivityDTO updateActivityDTO) throws NotFoundException {
        return activityMapper.toActivityDTO(activityService.updateActivity(id, updateActivityDTO));
    }

    @Operation(summary = "Elimina una actividad por medio de su ID", responses = {
        @ApiResponse(responseCode = "204", description = "Actividad eliminada exitosamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado: requiere rol ADMIN"),
        @ApiResponse(responseCode = "404", description = "Actividad no encontrada"),
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteActivity(@PathVariable String id) throws NotFoundException {
        activityService.deleteActivity(id);
    }

}
