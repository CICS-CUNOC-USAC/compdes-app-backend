package com.compdes.registrationStatus.events.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.compdes.notifications.mails.services.MailService;
import com.compdes.registrationStatus.events.RegistrationApprovedEvent;

import lombok.RequiredArgsConstructor;

/**
 * Listener encargado de manejar eventos de aprobación de registro.
 * 
 * Este componente escucha eventos del tipo {@link RegistrationApprovedEvent} y,
 * al ser notificado, envía un correo electrónico al participante confirmando la
 * finalización del proceso de inscripción. La operación se ejecuta de manera
 * asincrónica
 * para no bloquear el flujo principal de la aplicación.
 * 
 * Utiliza el servicio {@link MailService} para realizar el envío de correos.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-09
 */
@Component
@RequiredArgsConstructor
public class RegistrationApprovedListener {

    private final MailService mailService;

    /**
     * Maneja el evento de aprobación de registro enviando un correo electrónico
     * de confirmación al participante.
     * 
     * Este método se ejecuta de manera asincrónica tras recibir el evento
     * {@link RegistrationApprovedEvent}.
     * 
     * @param event evento que contiene el correo del participante y el ID del
     *              usuario asociado
     */
    @Async
    @EventListener
    public void onRegistrationApproved(RegistrationApprovedEvent event) {
        mailService.sendRegistrationApprovedEmail(event.getParticipantEmail(),
                event.getParticipantFullName(), event.getUserId());
    }
}
