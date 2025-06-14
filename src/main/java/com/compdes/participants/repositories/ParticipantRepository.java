package com.compdes.participants.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.compdes.participants.models.entities.Participant;

/**
 * Repositorio para operaciones CRUD sobre la entidad {@link Participant}.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-30
 */
@Repository
public interface ParticipantRepository
        extends JpaRepository<Participant, String>, JpaSpecificationExecutor<Participant> {

    /**
     * Verifica si existe un participante con el correo electrónico especificado.
     *
     * @param email correo electrónico a verificar
     * @return true si existe un participante con ese correo, false en caso
     *         contrario
     */
    public Boolean existsByEmail(String email);

    /**
     * Verifica si existe un participante registrado con el documento de
     * identificación especificado.
     * 
     * @param identificationDocument el número o código del documento de
     *                               identificación a verificar
     * @return true si existe un participante con ese documento, false en caso
     *         contrario
     */
    public Boolean existsByIdentificationDocument(String identificationDocument);

    /**
     * Busca un participante por su documento de identificación.
     * 
     * @param identificationDocument documento de identificación a buscar
     * @return un {@link Optional} que contiene al participante si se encuentra, o
     *         vacío si no existe coincidencia
     */
    public Optional<Participant> findByIdentificationDocument(String identificationDocument);

    /**
     * Busca un participante asociado a un usuario del sistema por su nombre
     * de usuario.
     * 
     * @param username nombre de usuario del usuario asociado al participante
     * @return un {@link Optional} que contiene el participante si fue encontrado, o
     *         vacío si no existe
     */
    public Optional<Participant> findByCompdesUser_Username(String username);
}
