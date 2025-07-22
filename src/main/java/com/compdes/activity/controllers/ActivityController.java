package com.compdes.activity.controllers;

import com.compdes.activity.models.dto.request.UpdateCapacityActivityDTO;
import org.springframework.web.bind.annotation.*;

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

@RestController
@RequestMapping("/api/v1/activities")
@AllArgsConstructor
public class ActivityController {
    private final ActivityService activityService;
    private final ActivityMapper activityMapper;

    @Operation(summary = "Obtiene todas las actividades", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de actividades obtenida exitosamente"),
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ActivityDTO> getAllActivities() {
        return activityMapper.toActivityDTOList(activityService.getAllActivities());
    }

    @Operation(summary = "Crear nueva actividad", responses = {
            @ApiResponse(responseCode = "201", description = "Actividad creada exitosamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: requiere rol ADMIN"),
            @ApiResponse(responseCode = "404", description = "Aula no encontrada"),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public void createActivity(@RequestBody @Valid CreateActivityDTO createActivityDTO) throws NotFoundException {
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteActivity(@PathVariable String id) throws NotFoundException {
        activityService.deleteActivity(id);
    }

    @Operation(summary = "Actualiza la capacidad de una actividad por medio de su ID", responses = {
            @ApiResponse(responseCode = "200", description = "Actividad actualizada exitosamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: requiere rol ADMIN"),
            @ApiResponse(responseCode = "404", description = "Actividad no encontrada"),
    })
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public void updateActivityCapacity(@PathVariable String id,
                                      @RequestBody @Valid UpdateCapacityActivityDTO updateCapacityActivityDTO)
            throws NotFoundException {
        activityService.updateCapacityActivity(id, updateCapacityActivityDTO);
    }

}
