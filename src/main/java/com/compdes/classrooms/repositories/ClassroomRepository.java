package com.compdes.classrooms.repositories;

import com.compdes.classrooms.models.entities.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, String> {

    /**
     * Busca si existe un salon por nombre
     * */
    public Boolean existByName(String name);

    /**
     * Busca si existe un salon con un modulo especifico
     * */
    public Boolean existByModule();


    //public Boolean

}
