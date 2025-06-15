package com.compdes.moduleUni.services;

import com.compdes.common.exceptions.DuplicateResourceException;
import com.compdes.moduleUni.mappers.ModuleUniMapper;
import com.compdes.moduleUni.models.dto.request.CreateModuleUniDTO;
import com.compdes.moduleUni.models.dto.response.ResponseModuleUniDTO;
import com.compdes.moduleUni.models.entities.ModuleUni;
import com.compdes.moduleUni.repositories.ModuleUniRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
/**
 * Servicio encargado de gestionar la creación y persistencia de modulos
 * del sistema.
 *
 * Administra el registro de modulos validando duplicidad
 * de datos
 *
 * Las operaciones están transaccionalmente garantizadas, realizando rollback en
 * caso de excepción.
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-06-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ModuleUniService {
    private final ModuleUniMapper moduleUniMapper;
    private final ModuleUniRepository moduleUniRepository;

    /**
     * Crea un salon
     * */
    public ModuleUni createModule(CreateModuleUniDTO createModuleUniDTO) {
        ModuleUni module = moduleUniMapper.createModuleDtoToModule(createModuleUniDTO);
        if(moduleUniRepository.existsByName(createModuleUniDTO.getName())){
            throw new DuplicateResourceException(
            "No se puede completar el registro: el modulo ya esta registrado");
        }
        return moduleUniRepository.save(module);
    }

    /**
     * Retorna todos los modulos
     * */
    public List<ResponseModuleUniDTO> getAllModules(){
        List<ModuleUni> iterable = moduleUniRepository.findAll();
        return iterable.stream()
                .map(moduleUniMapper::moduleToResponseDto)
                .collect(Collectors.toList());
    }
}
