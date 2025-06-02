package com.compdes.activity.services;

import com.compdes.activity.mappers.ActivityMapper;
import com.compdes.activity.models.dto.request.CreateActivityDTO;
import com.compdes.activity.models.entities.Activity;
import com.compdes.activity.repositories.ActivityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    public Activity createActivity(CreateActivityDTO createActivityDTO) {
        Activity activity = activityMapper.toActivity(createActivityDTO);
        return activityRepository.save(activity);
    }
}
