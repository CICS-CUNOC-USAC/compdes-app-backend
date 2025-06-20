package com.compdes.moduleUni.repositories;

import com.compdes.moduleUni.models.entities.ModuleUni;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleUniRepository extends JpaRepository<ModuleUni, String> {
    /**
     * Busca si existe un salon por nombre
     * */
    public Boolean existsByName(String name);
}
