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

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createActivity(@RequestBody @Valid CreateActivityDTO createActivityDTO) {
        activityService.createActivity(createActivityDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ActivityDTO getActivityById(@PathVariable String id) throws NotFoundException {
        return activityMapper.toActivityDTO(activityService.getActivityById(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ActivityDTO updateActivity(@PathVariable String id,
            @RequestBody @Valid UpdateActivityDTO updateActivityDTO) throws NotFoundException {
        return activityMapper.toActivityDTO(activityService.updateActivity(id, updateActivityDTO));
    }

}
