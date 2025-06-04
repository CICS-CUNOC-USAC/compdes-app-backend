
package com.compdes.storedFiles.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.compdes.common.exceptions.FileStorageException;
import com.compdes.common.exceptions.NotFoundException;
import com.compdes.storedFiles.models.dto.internal.StoredFileInternalDTO;
import com.compdes.storedFiles.models.dto.request.SaveStoredFileDTO;
import com.compdes.storedFiles.models.entities.StoredFile;
import com.compdes.storedFiles.repositories.StoredFileRepository;
import com.compdes.storedFiles.utils.MultipartFileConverter;

import lombok.RequiredArgsConstructor;

/**
 * Servicio de alto nivel para la gestión de archivos almacenados.
 *
 * Coordina la persistencia de metadatos en base de datos y la manipulación
 * física
 * de archivos en el sistema de archivos, delegando responsabilidades a
 * {@link StoredFileRepository} y {@link StoredFileManager}.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-02
 */
@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
public class StoredFileService {

    private final StoredFileRepository storedFileRepository;
    private final StoredFileManager fileManager;
    private final MultipartFileConverter multipartConverter;

    public StoredFile saveFile(MultipartFile multipartFile) {
        //mandamos a convertir el mutipartFile a un DTO
        SaveStoredFileDTO saveStoredFileDTO = multipartConverter.convertMultipartFileToSaveStoredFileDTO(multipartFile);
        //retornamos el resultado de la llamada al metodo saveFile
        return saveFile(saveStoredFileDTO);
    }
    /**
     * Guarda un archivo en base de datos y en el sistema de archivos.
     *
     * Primero persiste la entidad StoredFile con los metadatos del archivo.
     * Luego guarda el contenido físico del archivo en disco.
     *
     * @param saveStoredFileDTO datos del archivo a guardar (nombre, MIME,
     *                          contenido)
     * @return {@link StoredFile} entidad StoredFile persistida
     * @throws FileStorageException     si ocurre un error al guardar el archivo en
     *                                  disco
     * @throws IllegalArgumentException si la extensión no es una
     *                                  imagen válida
     */
    public StoredFile saveFile(SaveStoredFileDTO saveStoredFileDTO) {

        String fileName = fileManager.getFileName();
        String fileExtension = fileManager.getValidatedFileExtension(saveStoredFileDTO.getOriginalFilename());
        String mimeType = fileManager.getValidatedFileMimeType(fileExtension);

        StoredFile storedFile = new StoredFile(fileName, fileExtension, mimeType);

        // guardamos el archivo
        storedFile = storedFileRepository.save(storedFile);
        // si se guardo entonces podemos guardar el archivo fisicamente
        fileManager.saveFile(saveStoredFileDTO.getFileStream(), storedFile.getFullName());
        return storedFile;
    }

    /**
     * Edita un archivo existente tanto en base de datos como en el sistema de
     * archivos.
     *
     * Actualiza los metadatos (MIME y extensión) y reemplaza el archivo físico en
     * disco.
     *
     * @param fileId            ID del archivo a editar
     * @param editStoredFileDTO nuevos datos del archivo (nombre, MIME, contenido)
     * @return {@link StoredFile} entidad StoredFile actualizada
     * @throws NotFoundException        si no se encuentra el archivo en la base de
     *                                  datos
     * @throws FileStorageException     si ocurre un error al actualizar el archivo
     *                                  en
     *                                  disco
     * @throws IllegalArgumentException si la extensión no es una
     *                                  imagen válida
     */
    public StoredFile editFile(String fileId, SaveStoredFileDTO editStoredFileDTO) throws NotFoundException {

        // mandamos a trer el file de la imagen
        StoredFile currentFile = findStoredFileById(fileId);

        String fileExtension = fileManager.getValidatedFileExtension(editStoredFileDTO.getOriginalFilename());
        String mimeType = fileManager.getValidatedFileMimeType(fileExtension);

        // editamos los metadatos del archivo
        currentFile.setMimeType(mimeType);
        currentFile.setExtension(fileExtension);

        // guardar la edicion
        currentFile = storedFileRepository.save(currentFile);

        // ahora debemos eliminar el archivo anterior y guardar el nuevo
        fileManager.updateFile(editStoredFileDTO.getFileStream(), currentFile.getFullName());
        return currentFile;
    }

    /**
     * Recupera un archivo almacenado incluyendo su contenido binario.
     *
     * Busca el archivo en la base de datos y luego carga el archivo físico como
     * arreglo de bytes.
     *
     * @param fileId ID del archivo a recuperar
     * @return {@link StoredFileInternalDTO} interno con los metadatos y el
     *         contenido del archivo
     * @throws NotFoundException    si no se encuentra el archivo en la base de
     *                              datos
     * @throws FileStorageException si ocurre un error al leer el archivo físico
     */
    public StoredFileInternalDTO getStoredFileById(String fileId) throws NotFoundException {
        // mandamos a trer el file de la imagen
        StoredFile currentFile = findStoredFileById(fileId);
        byte[] fileByteArray = fileManager.getFile(currentFile.getFullName());
        return new StoredFileInternalDTO(currentFile, fileByteArray);
    }

    /**
     * Busca un archivo almacenado por su ID en la base de datos.
     *
     * @param fileId ID del archivo a buscar
     * @return {@link StoredFile} correspondiente al ID
     * @throws NotFoundException si no se encuentra el archivo en la base de datos
     */
    private StoredFile findStoredFileById(String fileId) throws NotFoundException {
        // mandamos a trer el file de la imagen
        return storedFileRepository.findById(fileId).orElseThrow(
                () -> new NotFoundException("El archivo solicitado no existe en el sistema."));
    }

}
