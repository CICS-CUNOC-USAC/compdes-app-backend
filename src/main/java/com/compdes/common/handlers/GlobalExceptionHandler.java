package com.compdes.common.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.compdes.common.exceptions.DuplicateResourceException;
import com.compdes.common.exceptions.IncompleteDataException;
import com.compdes.common.exceptions.InvalidTokenException;
import com.compdes.common.exceptions.NotFoundException;
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

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleConstraintViolationException(ConstraintViolationException ex) {
        return new ErrorDTO("Error inesperado en la Base de Datos.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new ErrorDTO(argumentErrorExtractor.extractMethodArgumentError(ex));
    }

    @ExceptionHandler(IncompleteDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleIncompleteDataException(IncompleteDataException ex) {
        return new ErrorDTO(
                """
                        No se pudo completar la operación porque faltan datos obligatorios.
                        Por favor, revisa que toda la información requerida esté completa antes de continuar.
                        Si el problema persiste, contacta al equipo de soporte e indica el siguiente código de error: """
                        + ex.getCode());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleResourceNotFound(BadCredentialsException ex) {
        return new ErrorDTO("Autenticación fallida: El correo electrónico o la contraseña son incorrectos."
                + " Por favor, verifica tus credenciales e intenta de nuevo.");
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFoundException(NotFoundException ex) {
        return new ErrorDTO(ex.getMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleValidationExceptions(DuplicateResourceException ex) {
        return new ErrorDTO(ex.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleGlobalException(InvalidTokenException ex) {
        return new ErrorDTO(
                """
                        No se pudo validar tu sesión correctamente.
                        Te recomendamos cerrar sesión e intentar ingresar nuevamente.
                        Si el problema persiste, contacta al equipo de soporte e indica el siguiente código de error: """
                        + ex.getCode());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleGlobalException(Exception ex) {
        ex.printStackTrace();
        return new ErrorDTO("Ocurrió un error inesperado: " + ex.getMessage());
    }

}
