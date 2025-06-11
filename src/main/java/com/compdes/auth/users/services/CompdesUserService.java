package com.compdes.auth.users.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compdes.auth.roles.services.RoleService;
import com.compdes.auth.users.enums.RolesEnum;
import com.compdes.auth.users.models.dto.request.CreateNonParticipantCompdesUserDTO;
import com.compdes.auth.users.models.dto.request.CreateParticipanCompdestUserDTO;
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
    private final PasswordEncoder passwordEncoder;
    private final ParticipantService participantService;

    /**
     * Crea un nuevo usuario en el sistema a partir del DTO recibido.
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
    public CompdesUser createNonParticipantUser(CreateNonParticipantCompdesUserDTO createCompdesUserDTO)
            throws NotFoundException {

        // mandamos a traer el rol por su label
        RolesEnum role = roleService.findRoleByLabel(createCompdesUserDTO.getRoleLabel());
        // verificar que no se este intentnndo crear un participante
        if (role == RolesEnum.PARTICIPANT) {
            throw new IllegalArgumentException("El rol 'Participante' no puede asignarse manualmente.");
        }

        return buildAndSaveCompdesUser(null, createCompdesUserDTO.getUsername(), createCompdesUserDTO.getPassword(),
                role, null);
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
     */
    public CompdesUser initializeBlankParticipantUser(Participant participant) {
        // creamos una instancia de CompdesUser con el username y la password nulos
        // le asignamos el rol participante y le adjuntamos el participante
        CompdesUser compdesUser = new CompdesUser(null, null, null, RolesEnum.PARTICIPANT, participant);

        // persistimos el usuarioQA
        compdesUser = compdesUserRepository.save(compdesUser);

        // mandamos a asignar el usuario al participante
        participant = participantService.setUserParticipant(compdesUser, participant);
        return compdesUser;
    }

    /**
     * Finaliza el proceso de registro de un usuario participante asignándole un
     * nombre de usuario y una contraseña.
     * 
     * @param userId          ID del usuario previamente inicializado sin
     *                        credenciales
     * @param compdestUserDTO DTO que contiene las credenciales y el documento de
     *                        identificación a validar
     * @return el usuario participante creado y guardado en la base de datos
     * @throws NotFoundException        si no se encuentra el usuario con el ID
     *                                  proporcionado.
     * @throws IllegalStateException    si el participante aún no ha sido aprobado
     * @throws IllegalArgumentException si el documento de identificación no
     *                                  coincide con el del participante
     */
    public CompdesUser finalizeParticipantAccountCreation(String userId,
            CreateParticipanCompdestUserDTO compdestUserDTO) throws NotFoundException {
        // mandamos a traer el participante por su id
        try {
            // encontrar el usuario por su id
            CompdesUser blankUser = findUserById(userId);
            Participant participant = blankUser.getParticipant();
            ;

            // asegurarse que el participante ya esta aprovado
            if (participant == null || !participant.getRegistrationStatus().getIsApproved()) {
                throw new IllegalStateException(
                        "No se puede completar la operación porque el participante aún no ha sido aprobado. "
                                + "Por favor, espera la aprobación antes de continuar.");
            }

            // validar que el participante tenga el mismo doc de identificacion que el
            // proporcionado
            if (!participant.getIdentificationDocument().equals(compdestUserDTO.getIdentificationDocument())) {
                throw new IllegalArgumentException(
                        "El documento de identificación ingresado no coincide con el registrado durante tu inscripción. "
                                + "Si no recuerdas qué documento utilizaste, por favor contacta al equipo de soporte e indica tu correo electrónico asociado.");
            }

            // si todo bien entonces podemos crear el usuario
            return buildAndSaveCompdesUser(blankUser.getId(), participant.getEmail(), compdestUserDTO.getPassword(),
                    RolesEnum.PARTICIPANT, participant);
        } catch (NotFoundException e) {
            throw new NotFoundException(
                    "No se pudo completar la operación. "
                            + "Es posible que el enlace haya sido modificado o que haya ocurrido un problema con tu registro. "
                            + "Si ya fuiste notificado como aprobado y estás seguro de que este proceso te pertenece, "
                            + "contacta al equipo de soporte para recibir ayuda.");
        }
    }

    /**
     * Construye y persiste un nuevo usuario del sistema con las credenciales
     * proporcionadas.
     * 
     * @param compdesUserDTO objeto que contiene el nombre de usuario y la
     *                       contraseña en texto plano
     * @param role           rol del sistema que se asignará al nuevo usuario
     * @param participant    entidad de participante asociada, o {@code null} si no
     *                       aplica
     * @return el usuario del sistema creado y persistido
     * @throws DuplicateResourceException si ya existe un usuario con el mismo
     *                                    nombre de usuario
     */
    private CompdesUser buildAndSaveCompdesUser(String id, String username, String rawPassoword, RolesEnum role,
            Participant participant) {
        // verificar duplicado
        if (compdesUserRepository.existsByUsername(username)) {
            throw new DuplicateResourceException(
                    "El nombre de usuario ingresado ya está en uso. Por favor, elige uno diferente.");
        }

        // encriptar password
        String encryptedPassword = passwordEncoder.encode(rawPassoword);

        // construir y guardar el usuario
        CompdesUser user = new CompdesUser(id, username, encryptedPassword, role, participant);
        return compdesUserRepository.save(user);
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
