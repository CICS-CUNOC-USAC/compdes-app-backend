package com.compdes.storedFiles.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.compdes.storedFiles.models.entities.StoredFile;

/**
 * Repositorio para operaciones CRUD sobre la entidad {@link StoredFile}.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-02
 */
@Repository
public interface StoredFileRepository extends CrudRepository<StoredFile, String> {

}
