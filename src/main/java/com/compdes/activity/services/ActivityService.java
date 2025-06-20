package com.compdes.activity.services;

import com.compdes.activity.enums.ActivityType;
import com.compdes.activity.mappers.ActivityMapper;
import com.compdes.activity.models.dto.request.CreateActivityDTO;
import com.compdes.activity.models.dto.request.UpdateActivityDTO;
import com.compdes.activity.models.entities.Activity;
import com.compdes.activity.repositories.ActivityRepository;
import com.compdes.classrooms.mappers.ClassroomMapper;
import com.compdes.classrooms.models.entities.Classroom;
import com.compdes.classrooms.services.ClassroomService;
import com.compdes.common.exceptions.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ClassroomService classroomService;
    private final ActivityMapper activityMapper;

    public List<Activity> getAllActivities() {
        return (List<Activity>) activityRepository.findAll();
    }

    public Activity createActivity(CreateActivityDTO createActivityDTO) throws NotFoundException {
        Activity activity = activityMapper.toActivity(createActivityDTO);
        Classroom classroom = classroomService.getClassroomById(createActivityDTO.getClassroomId());
        activity.setClassroom(classroom);
        return activityRepository.save(activity);
    }

    public Activity updateActivity(String id, UpdateActivityDTO updateActivityDTO) throws NotFoundException {
        Activity existingActivity = activityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Actividad no encontrada por medio del ID: " + id));

        existingActivity.setName(updateActivityDTO.getName());
        existingActivity.setDescription(updateActivityDTO.getDescription());
        existingActivity.setType(updateActivityDTO.getType());
        existingActivity.setScheduledDate(updateActivityDTO.getScheduledDate());

        return activityRepository.save(existingActivity);
    }

    public void deleteActivity(String id) throws NotFoundException {
        Activity existingActivity = activityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Actividad no encontrada por medio del ID: " + id));
        activityRepository.delete(existingActivity);
    }

    public Activity getActivityById(String id) throws NotFoundException {
        return activityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Actividad no encontrada por medio del ID: " + id));
    }
}
