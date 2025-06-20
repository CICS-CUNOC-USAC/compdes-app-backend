package com.compdes.classrooms.services;

import com.compdes.classrooms.mappers.ClassroomMapper;
import com.compdes.classrooms.models.dto.request.AvailableClassroomsDTO;
import com.compdes.classrooms.models.dto.request.CreateClassroomDTO;
import com.compdes.classrooms.models.dto.response.ResponseClassroomDTO;
import com.compdes.classrooms.models.entities.Classroom;
import com.compdes.classrooms.repositories.ClassroomRepository;
import com.compdes.common.exceptions.DuplicateResourceException;
import com.compdes.common.exceptions.NotFoundException;
import com.compdes.moduleUni.models.entities.ModuleUni;
import com.compdes.moduleUni.repositories.ModuleUniRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    private final ModuleUniRepository moduleUniRepository;

    /**
     * Crea un nuevo salón de clases para conferencias.
     * @param createClassroomDTO
     * @return
     * @throws NotFoundException
     */
    public Classroom createClassroom(CreateClassroomDTO createClassroomDTO) throws NotFoundException {
        ModuleUni module = moduleUniRepository.findById(createClassroomDTO.getModuleId())
                .orElseThrow(() -> new NotFoundException("Módulo no encontrado"));

        Classroom classroom = new Classroom(createClassroomDTO.getName(), module);
        if (classroomRepository.existsByName(classroom.getName())
                && classroomRepository.existsByModuleUni(classroom.getModuleUni())) {
            throw new DuplicateResourceException(
                    "No se puede completar el registro: el salon ya esta registrado");
        }
        return classroomRepository.save(classroom);
    }

    /**
     * Obtiene todos los salones registrados en el sistema
     * @return List<ResponseClassroomDTO>
     */
    public List<ResponseClassroomDTO> getAllClassrooms() {
        List<Classroom> iterable = classroomRepository.findAll();
        return classroomMapper.classroomToResponseDto(iterable);

    }

    /**
     * Obtiene un salon por medio de su ID
     * 
     * @param id
     * @return Classroom
     * @throws NotFoundException
     */
    public Classroom getClassroomById(String id) throws NotFoundException {
        return classroomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Salon no encontrado por medio del ID: " + id));
    }

    /**
     * Obtiene una lista de salones disponibles
     * 
     * @param initScheduledDate
     * @param endScheduledDate
     * @return
     */
    public List<ResponseClassroomDTO> getAvailableClassrooms(AvailableClassroomsDTO availableClassroomsDTO) {
        // Verificamos que las fechas de inicio y fin no sean nulas
        if(availableClassroomsDTO.getInitScheduledDate().isAfter(availableClassroomsDTO.getEndScheduledDate())) {
            throw new IllegalStateException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        List<Classroom> availableClassrooms = classroomRepository.findAvailableClassrooms(
                availableClassroomsDTO.getInitScheduledDate(), availableClassroomsDTO.getEndScheduledDate());
        return availableClassrooms.stream()
                .map(classroomMapper::classroomToResponseDto)
                .collect(Collectors.toList());
    }

}
