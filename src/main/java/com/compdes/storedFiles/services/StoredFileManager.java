package com.compdes.storedFiles.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.compdes.common.exceptions.FileStorageException;
import com.compdes.common.exceptions.NotFoundException;
import com.compdes.common.exceptions.enums.FileStorageErrorEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * Servicio encargado de gestionar operaciones relacionadas con el
 * almacenamiento de archivos en el sistema.
 * 
 * Esta clase permite construir entidades de archivo, guardar archivos
 * físicamente en el sistema de archivos,
 * y recuperar flujos de lectura para acceder a archivos previamente
 * almacenados.
 * 
 * Todas las operaciones contemplan validaciones y control de errores mediante
 * excepciones personalizadas,
 * facilitando una gestión robusta y controlada de errores relacionados con
 * rutas inválidas, accesos no permitidos,
 * errores de entrada/salida, extensiones nulas y otros posibles escenarios.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-02
 */
@Service
@Slf4j
public class StoredFileManager {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final Map<String, String> EXTENSION_MIME = Map.ofEntries(
            Map.entry("jpg", "image/jpeg"),
            Map.entry("jpeg", "image/jpeg"),
            Map.entry("png", "image/png"));

    public String getFileName() {
        return UUID.randomUUID().toString();
    }

    /**
     * Extrae la extensión de un nombre de archivo.
     *
     * Busca el último punto en el nombre del archivo y retorna la cadena
     * correspondiente a la extensión en minúsculas.
     *
     * @param fileFullName nombre completo del archivo (incluyendo extensión)
     * @return la extensión del archivo en minúsculas
     * @throws FileStorageException     si el archivo no contiene una extensión
     *                                  válida
     * @throws IllegalArgumentException si la extensión no es una imagen válida
     */
    public String getValidatedFileExtension(String fileFullName) {
        // encontramos el index del punto dentro del nombre del archivo
        int dotIndex = fileFullName.lastIndexOf('.');

        try {
            String extension = fileFullName.substring(dotIndex + 1).toLowerCase();
            validateImageExtension(extension);// validamos la extension del archivo
            return extension;
        } catch (IndexOutOfBoundsException e) {// si se lanza esta excepcion, signofica que no hay una extension en el
                                               // archivo
            log.warn("No se pudo extraer una extensión válida del archivo: '{}'", fileFullName);
            throw FileStorageErrorEnum.FILE_EXTENSION_NULL_OR_INVALID.getFileStorageException();
        }
    }

    /**
     * 
     * 
     * @param extension
     * @throws IllegalArgumentException si la extensión no es una
     *                                  imagen válida
     * @return
     */
    public String getValidatedFileMimeType(String extension) {
        // validamos la extension del archivo
        validateImageExtension(extension);
        /// si no fallo entonces la extension es valida y podemos retornar el mime
        return EXTENSION_MIME.get(extension);
    }

    /**
     * Guarda un archivo en el sistema de archivos bajo un nombre único.
     * 
     * El archivo es copiado desde el {@link InputStream} proporcionado a la ruta
     * completa determinada por el nombre de archivo dado.
     * 
     * Se sobrescribirá cualquier archivo existente con el mismo nombre.
     * 
     * Lanza excepciones específicas encapsuladas si ocurren errores en el proceso,
     * como permisos denegados, rutas inválidas, operaciones no soportadas o
     * problemas de E/S.
     * 
     * @param fileInputStream flujo de entrada con el contenido del archivo
     * @param fileFullName    nombre completo del archivo (incluyendo extensión)
     *                        bajo el cual se almacenará
     * 
     * @throws FileStorageException si ocurre un error relacionado con la operación
     *                              de almacenamiento
     */
    public void saveFile(InputStream fileInputStream, String fileFullName) {
        // creamos la ruta completa
        Path filePath = getFullPath(fileFullName);

        // copiamos en un nuevo archivo el que creamos
        try {
            Files.copy(fileInputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (FileAlreadyExistsException e) {
            log.warn("Intento de sobrescribir un archivo ya existente: '{}'", filePath.toAbsolutePath());
            throw FileStorageErrorEnum.FILE_ALREADY_EXISTS.getFileStorageException();
        } catch (DirectoryNotEmptyException e) {
            log.warn("No se pudo sobrescribir porque el destino no está vacío: '{}'", filePath.toAbsolutePath());
            throw FileStorageErrorEnum.FILE_IO_EXCEPTION.getFileStorageException();
        } catch (UnsupportedOperationException e) {
            log.warn("Operación no soportada al guardar archivo en: '{}'", filePath.toAbsolutePath());
            throw FileStorageErrorEnum.UNSUPPORTED_FILE_OPERATION.getFileStorageException();
        } catch (SecurityException e) {
            log.warn("Permiso denegado al intentar guardar archivo en: '{}'", filePath.toAbsolutePath());
            throw FileStorageErrorEnum.FILE_SECURITY_EXCEPTION.getFileStorageException();
        } catch (IOException e) {
            log.warn("Error de entrada/salida al intentar guardar archivo en: '{}'. Error: {}",
                    filePath.toAbsolutePath(), e.getMessage());
            throw FileStorageErrorEnum.FILE_IO_EXCEPTION.getFileStorageException();
        }
    }

    /**
     * Actualiza un archivo existente escribiendo uno nuevo de forma segura.
     *
     * El nuevo archivo se escribe primero a un archivo temporal. Si la escritura es
     * exitosa,
     * reemplaza el archivo original. Si falla, el archivo anterior se mantiene
     * intacto.
     *
     * @param newFileInputStream flujo del nuevo archivo a guardar
     * @param fileFullName       nombre completo (incluyendo extensión) del archivo
     *                           a remplazar
     * @throws FileStorageException si ocurre algún error durante la actualización
     */
    public void updateFile(InputStream newFileInputStream, String fileFullName) {
        String tempFileFullName = fileFullName + ".temp";
        Path originalPath = getFullPath(fileFullName);
        Path tempPath = getFullPath(tempFileFullName);

        boolean tempCreated = false;

        try {
            // Escribimos en un archivo temporal
            Files.copy(newFileInputStream, tempPath, StandardCopyOption.REPLACE_EXISTING);
            tempCreated = true;
            // Reemplazamos el archivo original con el nuevo
            Files.move(tempPath, originalPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (FileAlreadyExistsException e) {
            log.warn("El archivo ya existe y no se puede reemplazar: '{}'. Error: {}", originalPath.toAbsolutePath(),
                    e.getMessage());
            throw FileStorageErrorEnum.FILE_ALREADY_EXISTS.getFileStorageException();
        } catch (DirectoryNotEmptyException e) {
            log.warn("No se pudo mover el archivo porque el directorio no está vacío: '{}'. Error: {}",
                    originalPath.toAbsolutePath(), e.getMessage());
            throw FileStorageErrorEnum.DIRECTORY_NOT_EMPTY.getFileStorageException();
        } catch (UnsupportedOperationException e) {
            log.warn("Operación no soportada al intentar mover archivo de '{}' a '{}'. Error: {}",
                    tempPath.toAbsolutePath(), originalPath.toAbsolutePath(), e.getMessage());
            throw FileStorageErrorEnum.UNSUPPORTED_FILE_OPERATION.getFileStorageException();
        } catch (SecurityException e) {
            log.warn("Permiso denegado al intentar reemplazar archivo en: '{}'. Error: {}",
                    originalPath.toAbsolutePath(), e.getMessage());
        } catch (IOException e) {
            log.warn("Error de entrada/salida al actualizar archivo. Temporal: '{}', Destino: '{}'. Error: {}",
                    tempPath.toAbsolutePath(), originalPath.toAbsolutePath(), e.getMessage());
            throw FileStorageErrorEnum.FILE_IO_EXCEPTION.getFileStorageException();
        } finally {
            if (tempCreated) {
                deleteFile(tempFileFullName);
            }
        }
    }

    /**
     * Elimina un archivo del sistema de archivos de forma segura.
     *
     * Intenta eliminar el archivo especificado si existe.
     *
     * @param fileFullName el nombre completo (incluyendo la extensión) del archivo
     *                     a eliminar
     * @throws FileStorageException si ocurre un error durante el proceso de
     *                              eliminación
     * @return {@code true} si el archivo fue eliminado, {@code false} si no existía
     */
    public Boolean deleteFile(String fileFullName) {
        // creamos la ruta completa
        Path filePath = getFullPath(fileFullName);

        try {
            // Eliminamos el archivo
            return Files.deleteIfExists(filePath);
        } catch (DirectoryNotEmptyException e) {
            log.warn("No se pudo eliminar el archivo porque el directorio no está vacío: '{}'. Error: {}",
                    filePath.toAbsolutePath(), e.getMessage());
            throw FileStorageErrorEnum.DIRECTORY_NOT_EMPTY.getFileStorageException();
        } catch (SecurityException e) {
            log.warn("Permiso denegado al intentar eliminar el archivo: '{}'. Error: {}", filePath.toAbsolutePath(),
                    e.getMessage());
            throw FileStorageErrorEnum.FILE_SECURITY_EXCEPTION.getFileStorageException();
        } catch (IOException e) {
            log.warn("Error de entrada/salida al intentar eliminar el archivo: '{}'. Error: {}",
                    filePath.toAbsolutePath(), e.getMessage());
            throw FileStorageErrorEnum.FILE_IO_EXCEPTION.getFileStorageException();
        }
    }

    /**
     * Recupera un flujo de entrada para acceder al contenido de un archivo
     * almacenado.
     *
     * Verifica que el archivo existe y es legible antes de intentar abrirlo.
     * Si cumple las condiciones, retorna un {@link InputStream} que permite leer el
     * contenido del archivo desde el inicio.
     *
     * Lanza excepciones personalizadas si el archivo no existe, no puede leerse,
     * o si ocurre un error durante la apertura del flujo.
     *
     * @param fileFullName nombre completo del archivo (incluyendo su extensión)
     * @return flujo de entrada del archivo solicitado
     * @throws FileStorageException si el archivo no existe, no es legible,
     *                              o se produce un error durante la apertura
     */
    public byte[] getFile(String fileFullName) throws NotFoundException {
        // Construimos la ruta completa a partir del directorio base
        Path fullPath = getFullPath(fileFullName);

        // Validación de existencia con manejo de seguridad
        try {
            if (!Files.exists(fullPath)) {
                throw new NotFoundException("El archivo no existe: " + fileFullName);
            }
        } catch (SecurityException e) {
            log.warn("Permiso denegado al verificar la existencia del archivo: '{}'. Error: {}",
                    fullPath.toAbsolutePath(), e.getMessage());
            throw FileStorageErrorEnum.FILE_SECURITY_EXCEPTION.getFileStorageException();
        }

        // Validación de legibilidad con manejo de seguridad
        try {
            if (!Files.isReadable(fullPath)) {
                throw new SecurityException("El archivo no es legible: " + fileFullName);
            }
        } catch (SecurityException e) {
            log.warn("Permiso denegado al verificar legibilidad del archivo: '{}'. Error: {}",
                    fullPath.toAbsolutePath(), e.getMessage());
            throw FileStorageErrorEnum.FILE_SECURITY_EXCEPTION.getFileStorageException();
        }
        try {
            // Devolvemos un InputStream para que el archivo pueda ser leído
            return Files.readAllBytes(fullPath);
        } catch (OutOfMemoryError e) {
            log.warn("El archivo es demasiado grande para cargarlo en memoria: '{}'. Error: {}",
                    fullPath.toAbsolutePath(), e.getMessage());
            throw FileStorageErrorEnum.FILE_READ_OUT_OF_MEMORY.getFileStorageException();
        } catch (SecurityException e) {
            log.warn("Permiso denegado al intentar leer el archivo: '{}'. Error: {}", fullPath.toAbsolutePath(),
                    e.getMessage());
            throw FileStorageErrorEnum.FILE_SECURITY_EXCEPTION.getFileStorageException();
        } catch (IOException e) {
            log.warn("Error de entrada/salida al intentar leer el archivo: '{}'. Error: {}", fullPath.toAbsolutePath(),
                    e.getMessage());
            throw FileStorageErrorEnum.FILE_IO_EXCEPTION.getFileStorageException();
        }
    }

    /**
     * Valida que la extensión del archivo sea una imagen permitida.
     *
     * @param extension extensión del archivo (en minúsculas)
     * @throws IllegalArgumentException si la extensión no es una imagen válida
     */
    private void validateImageExtension(String extension) {
        Boolean isAllowed = EXTENSION_MIME.containsKey(extension);

        if (!isAllowed) {
            throw new IllegalArgumentException("Extensión de imagen no permitida: " + extension);
        }
    }

    /**
     * Construye la ruta absoluta del archivo a partir del directorio base.
     * 
     * Trata la construcción completa de la ruta como una operación atómica,
     * y lanza una excepción de almacenamiento si la ruta es inválida.
     *
     * @param fileFullName nombre completo del archivo (nombre + extensión)
     * @return ruta absoluta del archivo como {@link Path}
     * @throws FileStorageException si la ruta no es válida
     */
    private Path getFullPath(String fileFullName) {
        Path path = getUploadPath();

        try {
            return path.resolve(fileFullName);
        } catch (InvalidPathException e) {
            log.warn("getFullPath() -> Nombre de archivo inválido al intentar resolver la ruta: '{}'. Error: {}",
                    fileFullName,
                    e.getMessage());
            throw FileStorageErrorEnum.FILE_INVALID_PATH.getFileStorageException();
        }
    }

    public Path getUploadPath() {
        final String method = "getUploadPath";
        // Verifica si la carpeta existe, si no, la crea
        Path uploadPath = null;

        try {
            uploadPath = Paths.get(uploadDir);
        } catch (InvalidPathException e) {
            log.warn("[{}] Ruta base inválida: '{}'. Error: {}", method, uploadDir, e.getMessage());
            throw FileStorageErrorEnum.FILE_INVALID_PATH.getFileStorageException();
        }

        try {
            if (Files.exists(uploadPath)) {
                return uploadPath;
            }
        } catch (SecurityException e) {
            log.warn("[{}] Permiso denegado al verificar existencia del directorio: '{}'. Error: {}", method,
                    uploadPath.toAbsolutePath(), e.getMessage());
            throw FileStorageErrorEnum.FILE_INVALID_PATH.getFileStorageException();
        }

        try {
            return Files.createDirectories(uploadPath);
        } catch (FileAlreadyExistsException e) {
            log.warn("[{}] El directorio ya existe y no puede ser creado: '{}'. Error: {}", method,
                    uploadPath.toAbsolutePath(), e.getMessage());
            throw FileStorageErrorEnum.FILE_ALREADY_EXISTS.getFileStorageException();
        } catch (UnsupportedOperationException e) {
            log.warn("[{}] Operación no soportada al crear el directorio: '{}'. Error: {}", method,
                    uploadPath.toAbsolutePath(), e.getMessage());
            throw FileStorageErrorEnum.UNSUPPORTED_FILE_OPERATION.getFileStorageException();
        } catch (SecurityException e) {
            log.warn("[{}] Permiso denegado al crear el directorio: '{}'. Error: {}", method,
                    uploadPath.toAbsolutePath(), e.getMessage());
            throw FileStorageErrorEnum.FILE_SECURITY_EXCEPTION.getFileStorageException();
        } catch (IOException e) {
            log.warn("[{}] Error de entrada/salida al crear el directorio: '{}'. Error: {}", method,
                    uploadPath.toAbsolutePath(), e.getMessage());
            throw FileStorageErrorEnum.FILE_IO_EXCEPTION.getFileStorageException();
        }
    }
}
