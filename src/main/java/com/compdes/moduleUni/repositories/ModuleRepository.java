package com.compdes.moduleUni.repositories;

import com.compdes.classrooms.models.entities.Classroom;
import com.compdes.moduleUni.models.entities.ModuleUni;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepository extends JpaRepository<ModuleUni, String> {
}
