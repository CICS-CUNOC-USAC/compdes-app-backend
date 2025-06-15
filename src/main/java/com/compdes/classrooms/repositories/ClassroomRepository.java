package com.compdes.classrooms.repositories;

import com.compdes.classrooms.models.entities.Classroom;
import com.compdes.moduleUni.models.entities.ModuleUni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, String> {

    /**
     * Busca si existe un salon por nombre
     * */
    public Boolean existsByName(String name);

    /**
     * Busca si existe un salon con un modulo especifico
     * */
    public Boolean existsByModuleUni(ModuleUni moduleUni);


    //public Boolean

}
