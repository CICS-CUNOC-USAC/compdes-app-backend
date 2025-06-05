package com.compdes.common.exceptions.enums;

import com.compdes.common.exceptions.FileStorageException;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum que contiene los errores específicos relacionados con el almacenamiento
 * de archivos.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-02
 **/
@AllArgsConstructor
@Getter
public enum FileStorageErrorEnum {

        FILE_ALREADY_EXISTS(
                        new FileStorageException(ErrorCodeMessageEnum.FILE_ALREADY_EXISTS.getCode(),
                                        ErrorCodeMessageEnum.FILE_ALREADY_EXISTS.getMessage())),

        DIRECTORY_NOT_EMPTY(
                        new FileStorageException(ErrorCodeMessageEnum.DIRECTORY_NOT_EMPTY.getCode(),
                                        ErrorCodeMessageEnum.DIRECTORY_NOT_EMPTY.getMessage())),

        UNSUPPORTED_FILE_OPERATION(
                        new FileStorageException(ErrorCodeMessageEnum.UNSUPPORTED_FILE_OPERATION.getCode(),
                                        ErrorCodeMessageEnum.UNSUPPORTED_FILE_OPERATION.getMessage())),

        FILE_SECURITY_EXCEPTION(
                        new FileStorageException(ErrorCodeMessageEnum.FILE_SECURITY_EXCEPTION.getCode(),
                                        ErrorCodeMessageEnum.FILE_SECURITY_EXCEPTION.getMessage())),

        FILE_IO_EXCEPTION(
                        new FileStorageException(ErrorCodeMessageEnum.FILE_IO_EXCEPTION.getCode(),
                                        ErrorCodeMessageEnum.FILE_IO_EXCEPTION.getMessage())),
        FILE_EXTENSION_NULL_OR_INVALID(
                        new FileStorageException(
                                        ErrorCodeMessageEnum.FILE_EXTENSION_NULL_OR_INVALID.getCode(),
                                        ErrorCodeMessageEnum.FILE_EXTENSION_NULL_OR_INVALID.getMessage())),

        FILE_INVALID_PATH(
                        new FileStorageException(ErrorCodeMessageEnum.FILE_INVALID_PATH.getCode(),
                                        ErrorCodeMessageEnum.FILE_INVALID_PATH.getMessage())),


        FILE_NOT_FOUND_FOR_DELETE(
                        new FileStorageException(ErrorCodeMessageEnum.FILE_NOT_FOUND_FOR_DELETE.getCode(),
                                        ErrorCodeMessageEnum.FILE_NOT_FOUND_FOR_DELETE.getMessage())),

        FILE_READ_OUT_OF_MEMORY(new FileStorageException(
                        ErrorCodeMessageEnum.FILE_READ_OUT_OF_MEMORY.getCode(),
                        ErrorCodeMessageEnum.FILE_READ_OUT_OF_MEMORY.getMessage())),
        FILE_INPUT_STREAM_ERROR(
                        new FileStorageException(
                                        ErrorCodeMessageEnum.FILE_INPUT_STREAM_ERROR.getCode(),
                                        ErrorCodeMessageEnum.FILE_INPUT_STREAM_ERROR.getMessage())),

        ;

        private final FileStorageException fileStorageException;
}
