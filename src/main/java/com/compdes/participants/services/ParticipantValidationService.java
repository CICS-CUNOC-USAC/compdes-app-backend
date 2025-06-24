package com.compdes.participants.services;

import org.springframework.stereotype.Component;

import com.compdes.common.exceptions.DuplicateResourceException;
import com.compdes.participants.repositories.ParticipantRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de validar reglas de unicidad para participantes.
 * 
 * Centraliza las validaciones relacionadas con correos electrónicos y
 * documentos
 * de identificación, tanto en creación como en actualización de registros.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-23
 */
@Component
@RequiredArgsConstructor
public class ParticipantValidationService {
    private final ParticipantRepository participantRepository;

    /**
     * Valida que no exista otro participante con el mismo correo electrónico.
     * 
     * @param email        correo electrónico a validar
     * @param errorMessage mensaje de error a lanzar si el correo ya está registrado
     * @throws DuplicateResourceException si ya existe un participante con el mismo
     *                                    correo
     */
    public void validateUniqueEmail(String email, String errorMessage) {
        if (participantRepository.existsByEmail(email)) {
            throw new DuplicateResourceException(errorMessage);
        }
    }

    /**
     * Valida que no exista otro participante con el mismo documento de
     * identificación.
     * 
     * @param document     documento de identificación a validar
     * @param errorMessage mensaje de error a lanzar si el documento ya está
     *                     registrado
     * @throws DuplicateResourceException si ya existe un participante con el mismo
     *                                    documento
     */
    public void validateUniqueDocument(String document, String errorMessage) {
        if (participantRepository.existsByIdentificationDocument(document)) {
            throw new DuplicateResourceException(errorMessage);
        }
    }

    /**
     * Valida que no exista otro participante con el mismo correo, excluyendo un ID
     * específico.
     * 
     * Útil al actualizar registros para permitir que el correo se mantenga si no
     * cambia.
     * 
     * @param email        correo electrónico a validar
     * @param excludedId   ID del participante actual que se debe excluir de la
     *                     búsqueda
     * @param errorMessage mensaje de error a lanzar si el correo ya está en uso por
     *                     otro
     * @throws DuplicateResourceException si el correo ya está registrado por otro
     *                                    participante
     */
    public void validateUniqueEmailExcludingId(String email, String excludedId, String errorMessage) {
        if (participantRepository.existsByEmailAndIdIsNot(email, excludedId)) {
            throw new DuplicateResourceException(errorMessage);
        }
    }

    /**
     * Valida que no exista otro participante con el mismo documento, excluyendo un
     * ID específico.
     * 
     * @param document     documento de identificación a validar
     * @param excludedId   ID del participante actual que se debe excluir de la
     *                     búsqueda
     * @param errorMessage mensaje de error a lanzar si el documento ya está en uso
     *                     por otro
     * @throws DuplicateResourceException si el documento ya está registrado por
     *                                    otro participante
     */
    public void validateUniqueDocumentExcludingId(String document, String excludedId, String errorMessage) {
        if (participantRepository.existsByIdentificationDocumentAndIdIsNot(document, excludedId)) {
            throw new DuplicateResourceException(errorMessage);
        }
    }
}
