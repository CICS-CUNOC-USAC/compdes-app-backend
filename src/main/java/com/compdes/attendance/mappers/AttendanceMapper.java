package com.compdes.attendance.mappers;

import com.compdes.attendance.models.dto.response.AttendaceDTO;
import com.compdes.attendance.models.entities.Attendance;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {
    public AttendaceDTO toAttendaceDTO(Attendance attendance);
    public List<AttendaceDTO> toAttendaceDTOList(List<Attendance> attendances);
}
