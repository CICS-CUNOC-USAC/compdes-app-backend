package com.compdes.activity.mappers;

import com.compdes.activity.models.dto.request.CreateActivityDTO;
import com.compdes.activity.models.dto.response.ActivityDTO;
import com.compdes.activity.models.entities.Activity;

import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityMapper {

    public ActivityDTO toActivityDTO(Activity activity);

    public Activity toActivity(CreateActivityDTO createActivityDTO);

    public List<ActivityDTO> toActivityDTOList(List<Activity> activities);
}
