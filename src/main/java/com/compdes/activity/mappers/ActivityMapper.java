package com.compdes.activity.mappers;

import com.compdes.activity.models.dto.request.CreateActivityDTO;
import com.compdes.activity.models.dto.response.ActivityDTO;
import com.compdes.activity.models.entities.Activity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityMapper {
    public ActivityDTO toActivityDTO(Activity activity);
    
    public Activity toActivity(CreateActivityDTO createActivityDTO);
}
