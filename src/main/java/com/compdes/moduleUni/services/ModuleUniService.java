package com.compdes.moduleUni.services;

import com.compdes.moduleUni.mappers.ModuleUniMapper;
import com.compdes.moduleUni.models.dto.request.CreateModuleDTO;
import com.compdes.moduleUni.models.dto.response.ResponseModuleDTO;
import com.compdes.moduleUni.models.entities.ModuleUni;
import com.compdes.moduleUni.repositories.ModuleRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ModuleUniService {
    private final ModuleUniMapper mapper;
    private final ModuleRepository repository;

    /**
     * Crea un salon
     * */
    public ModuleUni createModule(CreateModuleDTO createModuleDTO) {
        ModuleUni module = mapper.createModuleDtoToModule(createModuleDTO);
        //if(repository.exists(module)){
        //throw new DuplicateResourceException(
        //    "No se puede completar el registro: el module ya esta registrado");
        //}
        module = repository.save(module);
        return module;
    }

    public List<ResponseModuleDTO> getAllModules(){
        List<ModuleUni> iterable = repository.findAll();
        return iterable.stream()
                .map(mapper::moduleToResponseDto) // asumiendo que tienes este método
                .collect(Collectors.toList());
    }
}
