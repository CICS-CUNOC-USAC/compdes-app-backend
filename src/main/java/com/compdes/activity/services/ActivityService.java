package com.compdes.activity.services;

import com.compdes.activity.mappers.ActivityMapper;
import com.compdes.activity.models.dto.request.CreateActivityDTO;
import com.compdes.activity.models.dto.request.UpdateActivityDTO;
import com.compdes.activity.models.dto.request.UpdateCapacityActivityDTO;
import com.compdes.activity.models.entities.Activity;
import com.compdes.activity.repositories.ActivityRepository;
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

    /**
     * Obtiene todas las actividades
     * 
     * @return List<Activity>
     */
    public List<Activity> getAllActivities() {
        return (List<Activity>) activityRepository.findAll();
    }

    /**
     * Crea una nueva actividad
     * 
     * @param createActivityDTO
     * @return Activity
     * @throws NotFoundException
     */
    public Activity createActivity(CreateActivityDTO createActivityDTO) throws NotFoundException {
        // Verificamos que la hora de inicio sea menos que la hora de fin
        if (createActivityDTO.getInitScheduledDate().isAfter(createActivityDTO.getEndScheduledDate())) {
            throw new IllegalStateException("La fecha de inicio debe ser anterior a la fecha de fin.");
        }
        if (activityRepository.existsByClassroomIdAndInitScheduledDateLessThanEqualAndEndScheduledDateGreaterThanEqual(
                createActivityDTO.getClassroomId(),
                createActivityDTO.getInitScheduledDate(),
                createActivityDTO.getEndScheduledDate())) {
            throw new IllegalStateException(
                    "Ya existe una actividad programada en este horario para el aula especificada.");
        }
        Activity activity = activityMapper.toActivity(createActivityDTO);
        Classroom classroom = classroomService.getClassroomById(createActivityDTO.getClassroomId());
        activity.setClassroom(classroom);
        return activityRepository.save(activity);
    }

    /**
     * Actualiza una actividad existente
     * 
     * @param id
     * @param updateActivityDTO
     * @return Activity
     * @throws NotFoundException
     */
    public Activity updateActivity(String id, UpdateActivityDTO updateActivityDTO) throws NotFoundException {
        
        Activity existingActivity = activityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Actividad no encontrada por medio del ID: " + id));
        // Verificamos que la hora de inicio sea menos que la hora de fin
        if (updateActivityDTO.getInitScheduledDate().isAfter(updateActivityDTO.getEndScheduledDate())) {
            throw new IllegalStateException("La fecha de inicio debe ser anterior a la fecha de fin.");
        }
        if (activityRepository
                .existsByIdNotAndClassroomIdAndInitScheduledDateLessThanEqualAndEndScheduledDateGreaterThanEqual(
                        existingActivity.getId(),
                        existingActivity.getClassroom().getId(),
                        updateActivityDTO.getInitScheduledDate(),
                        updateActivityDTO.getEndScheduledDate())) {
            throw new IllegalStateException(
                    "Ya existe una actividad programada en este horario para el aula especificada."
                            + " Por favor, verifica las fechas y horarios de la actividad.");
        }

        Classroom classroom = classroomService.getClassroomById(updateActivityDTO.getClassroomId());
        existingActivity.setName(updateActivityDTO.getName());
        existingActivity.setDescription(updateActivityDTO.getDescription());
        existingActivity.setType(updateActivityDTO.getType());
        existingActivity.setInitScheduledDate(updateActivityDTO.getInitScheduledDate());
        existingActivity.setEndScheduledDate(updateActivityDTO.getEndScheduledDate());
        existingActivity.setClassroom(classroom);

        return activityRepository.save(existingActivity);
    }

    /**
     * Elimina una actividad por medio de su ID
     * @param id
     * @throws NotFoundException
     */
    public void deleteActivity(String id) throws NotFoundException {
        Activity existingActivity = activityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Actividad no encontrada por medio del ID: " + id));
        activityRepository.delete(existingActivity);
    }

    /**
     * Obtiene una actividad por medio de su ID
     * @param id
     * @return
     * @throws NotFoundException
     */
    public Activity getActivityById(String id) throws NotFoundException {
        return activityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Actividad no encontrada por medio del ID: " + id));
    }

    public Activity updateCapacityActivity(String id, UpdateCapacityActivityDTO updateCapacityActivityDTO)
            throws NotFoundException {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Actividad no encontrada por medio del ID: " + id));
        if (updateCapacityActivityDTO.getCapacity() < 0) {
            throw new IllegalStateException("La capacidad debe ser un numero positivo");
        }
        activity.setCapacity(updateCapacityActivityDTO.getCapacity());
        return activityRepository.save(activity);
    }
}
