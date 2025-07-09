package com.compdes.classrooms.repositories;

import com.compdes.classrooms.models.entities.Classroom;
import com.compdes.moduleUni.models.entities.ModuleUni;

import java.time.LocalDateTime;
import java.util.List;

import com.compdes.participants.models.entities.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Repositorio para operaciones CRUD sobre la entidad {@link Classroom}.
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-06-30
 */
public interface ClassroomRepository extends JpaRepository<Classroom, String> {

    @Query("SELECT c FROM Classroom c WHERE c.id NOT IN (" +
            "SELECT a.classroom.id FROM Activity a " +
            "WHERE a.initScheduledDate < :endTime AND a.endScheduledDate > :startTime)")
    List<Classroom> findAvailableClassrooms(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Busca si existe un salon por nombre
     */
    public Boolean existsByName(String name);

    /**
     * Busca si existe un salon con un modulo especifico
     */
    public Boolean existsByModuleUni(ModuleUni moduleUni);

}
