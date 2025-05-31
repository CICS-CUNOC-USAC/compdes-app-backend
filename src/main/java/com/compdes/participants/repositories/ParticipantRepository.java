package com.compdes.participants.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.compdes.participants.models.entities.Participant;

/**
 * Repositorio para la gestión de entidades {@link Participant}.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-30
 */
@Repository
public interface ParticipantRepository extends CrudRepository<Participant, String> {

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
}
