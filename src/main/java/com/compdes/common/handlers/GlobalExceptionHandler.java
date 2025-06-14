package com.compdes.common.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.compdes.common.exceptions.CustomRuntimeException;
import com.compdes.common.exceptions.DuplicateResourceException;
import com.compdes.common.exceptions.FileStorageException;
import com.compdes.common.exceptions.InvalidTokenException;
import com.compdes.common.exceptions.NotFoundException;
import com.compdes.common.exceptions.QrCodeException;
import com.compdes.common.models.dto.response.ErrorDTO;
import com.compdes.common.utils.MethodArgumentErrorExtractor;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-31
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MethodArgumentErrorExtractor argumentErrorExtractor;

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ErrorDTO(ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleConstraintViolationException(ConstraintViolationException ex) {
        return new ErrorDTO("Error inesperado en la Base de Datos.");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        return new ErrorDTO("El archivo supera el tamaño máximo permitido.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ErrorDTO(argumentErrorExtractor.extractMethodArgumentError(ex));
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleBadCredentialsException(BadCredentialsException ex) {
        return new ErrorDTO("Autenticación fallida: El correo electrónico o la contraseña son incorrectos."
                + " Por favor, verifica tus credenciales e intenta de nuevo.");
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        return new ErrorDTO("No tienes autorización para acceder a este recurso. "
                + "Verifica que tengas los permisos adecuados o contacta al equipo de soporte si consideras que esto es un error.");
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFoundException(NotFoundException ex) {
        return new ErrorDTO(ex.getMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleDuplicateResourceException(DuplicateResourceException ex) {
        return new ErrorDTO(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleIllegalStateException(IllegalStateException ex) {
        return new ErrorDTO(ex.getMessage());
    }

    @ExceptionHandler(QrCodeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleQrCodeException(QrCodeException ex) {
        return new ErrorDTO(
                """
                        Ocurrió un error al procesar el código QR.
                        Por favor, intenta nuevamente o contacta al equipo de soporte con el siguiente código de error: """
                        + ex.getCode());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleInvalidTokenException(InvalidTokenException ex) {
        return new ErrorDTO(
                """
                        No se pudo validar tu sesión correctamente.
                        Te recomendamos cerrar sesión e intentar ingresar nuevamente.
                        Si el problema persiste, contacta al equipo de soporte e indica el siguiente código de error: """
                        + ex.getCode());
    }

    @ExceptionHandler(FileStorageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleFileStorageException(FileStorageException ex) {
        return new ErrorDTO(
                """
                        Ocurrió un error al procesar el archivo. Por favor, intenta la operación nuevamente.
                        Si el problema persiste, contacta al equipo de soporte e indica el siguiente código de error: """
                        + ex.getCode());
    }

    @ExceptionHandler(CustomRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleCustomRuntimeException(CustomRuntimeException ex) {
        return new ErrorDTO(
                """
                        Ocurrió un error inesperado al procesar la información.
                        Por favor, contacta al equipo de soporte e indica el siguiente código de error: """
                        + ex.getCode());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleGlobalException(Exception ex) {
        ex.printStackTrace();
        return new ErrorDTO("Ocurrió un error inesperado: " + ex.getMessage());
    }

}
