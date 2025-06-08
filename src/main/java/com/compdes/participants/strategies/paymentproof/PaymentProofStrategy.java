package com.compdes.participants.strategies.paymentproof;

import com.compdes.participants.models.dto.internal.CreateParticipantInternalDTO;
import com.compdes.participants.models.entities.Participant;

/**
 * Estrategia para el procesamiento de comprobantes de pago de participantes.
 * 
 * Define el contrato que deben implementar las distintas estrategias que
 * manejan la forma en que se procesa y asocia un comprobante de pago a un
 * participante, ya sea como archivo de imagen o como enlace.
 * 
 * Esta interfaz facilita la aplicación del patrón Strategy para separar la
 * lógica específica de cada tipo de comprobante.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-08
 */

public interface PaymentProofStrategy {

    /**
     * Procesa y asocia un comprobante de pago al participante.
     * 
     * La implementación concreta de esta estrategia define cómo se debe tratar
     * el comprobante de pago contenido en el DTO, ya sea como una imagen o como
     * un enlace, y lo asigna al participante correspondiente.
     * 
     * @param participant participante al que se asociará el comprobante de pago
     * @param dto         objeto que contiene la información de creación del
     *                    participante,
     *                    incluyendo el comprobante de pago
     */
    public void process(Participant participant, CreateParticipantInternalDTO dto);
}
