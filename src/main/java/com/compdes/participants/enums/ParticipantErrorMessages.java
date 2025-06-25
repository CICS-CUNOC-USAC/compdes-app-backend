package com.compdes.participants.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ParticipantErrorMessages {
        NOT_FOUND_BY_DOCUMENT(
                        "No se encontró ninguna inscripción asociada al documento de identificación ingresado. " +
                                        "Por favor, verifica que los datos sean correctos. " +
                                        "Si estás seguro de que te inscribiste, pero aún así no puedes ver tu información, contacta al equipo de soporte."),

        NOT_FOUND_BY_USERNAME(
                        "No se encontró ninguna inscripción vinculada al usuario ingresado. " +
                                        "Verifica tus datos o contacta al equipo de soporte si ya realizaste tu inscripción."),
        NOT_FOUND_BY_ID(
                        "No se encontró un participante con el ID proporcionado. " +
                                        "Por favor, verifica la información e intenta nuevamente."),

        DUPLICATE_DOCUMENT(
                        "Este número de documento ya está asociado a otro participante.  Por favor, verifica que sea correcto."),

        DUPLICATE_EMAIL(
                        "Ya existe un participante registrado con este correo electrónico. Por favor, verifica que sea correcto."),
        PARTICIPANT_ALREADY_CONFIRMED(
                        "No es posible realizar esta operación porque el participante ya fue confirmado previamente."),
        NOT_FOUND_BY_QR(
                        "No se encontró un participante asociado al código QR proporcionado. " +
                                        "Si crees que esto es un error, por favor contacta al equipo de soporte.");

        private final String message;
}
