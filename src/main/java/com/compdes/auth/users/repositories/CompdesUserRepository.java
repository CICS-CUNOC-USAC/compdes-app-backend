package com.compdes.auth.users.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.compdes.auth.users.models.entities.CompdesUser;

/**
 * Repositorio para operaciones CRUD sobre la entidad {@link CompdesUser}.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-01
 */
@Repository
public interface CompdesUserRepository extends CrudRepository<CompdesUser, String> {

    /**
     * Verifica si existe un usuario registrado con el nombre de usuario
     * proporcionado.
     * 
     * @param username nombre de usuario a verificar
     * @return {@code true} si ya existe un usuario con ese nombre, {@code false} en
     *         caso contrario
     */
    public Boolean existsByUsername(String username);

    /**
     * Busca un usuario por su nombre de usuario.
     * 
     * @param username nombre de usuario a buscar
     * @return un {@code Optional} que contiene el usuario si fue encontrado, o
     *         vacío si no existe
     */
    public Optional<CompdesUser> findUserByUsername(String username);
}
