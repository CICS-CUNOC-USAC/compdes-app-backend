package com.compdes.auth.users.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compdes.auth.roles.services.RoleService;
import com.compdes.auth.users.enums.RolesEnum;
import com.compdes.auth.users.mappers.CompdesUserMapper;
import com.compdes.auth.users.models.dto.request.CreateCompdesUserDTO;
import com.compdes.auth.users.models.entities.CompdesUser;
import com.compdes.auth.users.repositories.CompdesUserRepository;
import com.compdes.common.exceptions.DuplicateResourceException;
import com.compdes.common.exceptions.NotFoundException;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.services.ParticipantService;

import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public class CompdesUserService {

    private final CompdesUserRepository compdesUserRepository;
    private final RoleService roleService;
    private final CompdesUserMapper compdesUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final ParticipantService participantService;

    /**
     * Crea un nuevo usuario en el sistema a partir del DTO recibido.
     * 
     * Mapea los datos, verifica duplicidad por username, encripta la contraseña
     * y persiste el usuario en la base de datos.
     * 
     * @param createCompdesUserDTO datos del usuario a registrar
     * @return el usuario creado y guardado
     * @throws NotFoundException          si no se encuentra ningún rol con la
     *                                    etiqueta
     *                                    proporcionada
     * @throws DuplicateResourceException si el username ya está en uso
     * @throws IllegalArgumentException   si se trata de asignar el role
     *                                    {@code RolesEnum#PARTICIPANT}
     */
    public CompdesUser createNonParticipantUser(CreateCompdesUserDTO createCompdesUserDTO) throws NotFoundException {

        // mandamos a traer el rol por su label
        RolesEnum role = roleService.findRoleByLabel(createCompdesUserDTO.getRoleLabel());

        if (role == RolesEnum.PARTICIPANT) {
            throw new IllegalArgumentException("El rol 'Participante' no puede asignarse manualmente.");
        }

        // mapeamos y asignamos el role
        CompdesUser compdesUser = compdesUserMapper.createCompdesUserDtoToCompdesUser(createCompdesUserDTO);
        compdesUser.setRole(role);
        compdesUser.setParticipant(null);

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
     * Inicializa un usuario del sistema asociado a un participante con credenciales
     * vacías.
     * 
     * Este método crea una instancia de {@link CompdesUser} con valores nulos para
     * el
     * nombre de usuario y la contraseña, asignándole el rol de PARTICIPANT y
     * asociándolo.
     * 
     * <strong>Nota:</strong> Este método forma parte de la lógica interna del
     * sistema y
     * no está diseñado para ser expuesto como un endpoint HTTP.
     * 
     * @param participant entidad del participante al que se asociará el usuario
     * @return el usuario del sistema creado y asociado al participante
     * @throws IllegalStateException si el participante ya tiene un usuario asignado
     */
    public CompdesUser initializeBlankParticipantUser(Participant participant) {
        // creamos una instancia de CompdesUser con el username y la password nulos
        // le asignamos el rol participante y le adjuntamos el participante
        CompdesUser compdesUser = new CompdesUser(null, null, RolesEnum.PARTICIPANT, participant);

        // persistimos el usuario
        compdesUser = compdesUserRepository.save(compdesUser);

        // mandamos a asignar el usuario al participante
        participant = participantService.setUserParticipant(compdesUser, participant);
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

    public Long count() {
        return compdesUserRepository.count();
    }

}
