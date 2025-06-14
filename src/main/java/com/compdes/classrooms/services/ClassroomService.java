package com.compdes.classrooms.services;

import com.compdes.classrooms.mappers.ClassroomMapper;
import com.compdes.classrooms.models.dto.request.CreateClassroomDTO;
import com.compdes.classrooms.models.dto.response.ResponseClassroomDTO;
import com.compdes.classrooms.models.entities.Classroom;
import com.compdes.classrooms.repositories.ClassroomRepository;
import com.compdes.common.exceptions.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Servicio encargado de gestionar la creación y persistencia de salones
 * del sistema.
 *
 * Administra el registro de salones para conferencias, validando duplicidad
 * de datos
 *
 * Las operaciones están transaccionalmente garantizadas, realizando rollback en
 * caso de excepción.
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-06-03
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ClassroomService {
    private final ClassroomMapper classroomMapper;
    private final ClassroomRepository classroomRepository;

    /**
    * Crea un salon
    * */
    public Classroom createClassroom(CreateClassroomDTO createClassroomDTO) {
        Classroom classroom = classroomMapper.createClassroomDtoToClassroom(createClassroomDTO);
        if(classroomRepository.exists(classroom)){
            throw new DuplicateResourceException(
                "No se puede completar el registro: el salon ya esta registrado"
            );
        }
        classroom = classroomRepository.save(classroom);
        return classroom;
    }

    /**
    * obtiene todos los salones y los convierte en un tipo de dato DTO
    * */
    public List<ResponseClassroomDTO> getAllClassrooms() {
        List<Classroom> iterable = classroomRepository.findAll();
        return iterable.stream()
                .map(classroomMapper::classroomToResponseDto) // asumiendo que tienes este método
                .collect(Collectors.toList());
    }

}
