package com.compdes.registrationStatus.events;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Evento que representa la aprobación del registro de un participante.
 * 
 * Esta clase es utilizada para notificar que el proceso de inscripción de un
 * usuario
 * ha sido completado y aprobado. Contiene la información mínima necesaria para
 * desencadenar acciones posteriores, como el envío de correos electrónicos u
 * otras
 * tareas asincrónicas.
 * 
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-09
 */
@AllArgsConstructor
@Value
public class RegistrationApprovedEvent {
    String userId;
    String participantEmail;
    String participantFullName;
}
