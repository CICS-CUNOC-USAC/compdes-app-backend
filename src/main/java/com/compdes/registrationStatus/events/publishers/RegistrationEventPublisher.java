package com.compdes.registrationStatus.events.publishers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.compdes.registrationStatus.events.RegistrationApprovedEvent;

import lombok.RequiredArgsConstructor;

/**
 * Componente encargado de publicar eventos relacionados con el estado de
 * registro.
 * 
 * Utiliza el mecanismo de eventos de Spring a través de
 * {@link ApplicationEventPublisher}.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-09
 */
@Component
@RequiredArgsConstructor
public class RegistrationEventPublisher {

    private final ApplicationEventPublisher publisher;

    /**
     * Publica un evento que indica que el registro de un participante ha sido
     * confirmado.
     * 
     * Este evento puede ser manejado por cualquier componente que escuche eventos
     * del tipo {@link RegistrationApprovedEvent}, permitiendo acciones asincrónicas
     * tras la confirmación del registro.
     * 
     * @param event evento que contiene los datos del participante cuyo registro fue
     *              aprobado
     */
    public void publishRegistrationApproved(RegistrationApprovedEvent event) {
        publisher.publishEvent(event);
    }

}
