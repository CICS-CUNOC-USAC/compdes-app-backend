package com.compdes.auth.users.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compdes.auth.users.mappers.CompdesUserMapper;
import com.compdes.auth.users.models.dto.request.CreateCompdesUserDTO;
import com.compdes.auth.users.models.entities.CompdesUser;
import com.compdes.auth.users.repositories.CompdesUserRepository;
import com.compdes.common.exceptions.DuplicateResourceException;
import com.compdes.common.exceptions.NotFoundException;

import lombok.AllArgsConstructor;

/**
 * Servicio encargado de gestionar la creación y persistencia de usuarios del
 * sistema Compdes.
 * 
 * Encapsula la lógica de negocio asociada al registro de nuevos usuarios,
 * incluyendo validaciones, asignación de roles y posterior persistencia.
 * 
 * Este servicio actúa como orquestador entre el repositorio de usuarios y la
 * lógica de autenticación,
 * asegurando la consistencia de los datos y preparando la información para
 * mecanismos de seguridad como JWT.
 * 
 * Puede extenderse para incluir operaciones adicionales como actualización de
 * credenciales,
 * cambio de roles, recuperación de contraseñas o eliminación lógica de
 * usuarios.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-01
 */
@Service
@Transactional(rollbackFor = Exception.class)
@AllArgsConstructor
public class CompdesUserService {

    private final CompdesUserRepository compdesUserRepository;
    private final CompdesUserMapper compdesUserMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Crea un nuevo usuario en el sistema a partir del DTO recibido.
     * 
     * Mapea los datos, verifica duplicidad por username, encripta la contraseña
     * y persiste el usuario en la base de datos.
     * 
     * @param createCompdesUserDTO datos del usuario a registrar
     * @return el usuario creado y guardado
     * @throws DuplicateResourceException si el username ya está en uso
     */
    public CompdesUser createUser(CreateCompdesUserDTO createCompdesUserDTO) {
        // mapeamos
        CompdesUser compdesUser = compdesUserMapper.createCompdesUserDtoToCompdesUser(createCompdesUserDTO);

        // verficar que no exista otro usuario con el mismo username
        if (compdesUserRepository.existsByUsername(compdesUser.getUsername())) {
            throw new DuplicateResourceException(
                    "El nombre de usuario ingresado ya está en uso. Por favor, elige uno diferente.");
        }

        // encriptar la password
        compdesUser.setPassword(passwordEncoder.encode(compdesUser.getPassword()));

        // si nada falla entonces guardamos
        compdesUser = compdesUserRepository.save(compdesUser);

        return compdesUser;
    }

    /**
     * Busca un usuario por su ID.
     * 
     * @param id identificador único del usuario
     * @return el usuario encontrado
     * @throws NotFoundException si no existe un usuario con el ID proporcionado
     */
    public CompdesUser findUserById(String id) throws NotFoundException {
        CompdesUser compdesUser = compdesUserRepository.findById(id).orElseThrow(
                () -> new NotFoundException("No se encontró un usuario con el ID proporcionado."));
        return compdesUser;
    }

    /**
     * Busca un usuario por su nombre de usuario.
     * 
     * @param username nombre de usuario a buscar
     * @return el usuario encontrado
     * @throws NotFoundException si no existe un usuario con el nombre proporcionado
     */
    public CompdesUser findUserByUsername(String username) throws NotFoundException {
        CompdesUser compdesUser = compdesUserRepository.findUserByUsername(username).orElseThrow(
                () -> new NotFoundException("No se encontró un usuario con el ID proporcionado."));
        return compdesUser;
    }

}
