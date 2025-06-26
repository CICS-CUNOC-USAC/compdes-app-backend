package com.compdes.moduleUni.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compdes.common.exceptions.DuplicateResourceException;
import com.compdes.moduleUni.mappers.ModuleUniMapper;
import com.compdes.moduleUni.models.dto.request.CreateModuleUniDTO;
import com.compdes.moduleUni.models.dto.response.ResponseModuleUniDTO;
import com.compdes.moduleUni.models.entities.ModuleUni;
import com.compdes.moduleUni.repositories.ModuleUniRepository;

import lombok.RequiredArgsConstructor;

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
     */
    public ModuleUni createModule(CreateModuleUniDTO createModuleUniDTO) {
        ModuleUni module = moduleUniMapper.createModuleDtoToModule(createModuleUniDTO);
        if (moduleUniRepository.existsByName(createModuleUniDTO.getName())) {
            throw new DuplicateResourceException(
                    "No se puede completar el registro: el modulo ya esta registrado");
        }
        return moduleUniRepository.save(module);
    }

    /**
     * Obtiene una lista paginada de todos los módulos universitarios.
     * 
     * @param pageable objeto que contiene la información de paginación y
     *                 ordenamiento
     * @return una página de objetos {@link ResponseModuleUniDTO} con los módulos
     *         encontrados
     */
    public Page<ModuleUni> getPaginatedModules(Pageable pageable) {
        return moduleUniRepository.findAll(pageable);
    }

    /**
     * Retorna todos los modulos
     * 
     * @return Lista que contiene los modulos disponibles
     */
    public List<ModuleUni> getAllModules() {
        return moduleUniRepository.findAll();
    }
}
