package com.compdes.participants.strategies.paymentproof;

import org.springframework.stereotype.Component;

import com.compdes.participants.models.dto.internal.CreateParticipantInternalDTO;
import com.compdes.participants.models.entities.Participant;
import com.compdes.paymentProofs.models.entities.PaymentProof;
import com.compdes.paymentProofs.services.PaymentProofService;

import lombok.RequiredArgsConstructor;

/**
 * Estrategia para procesar la prueba de pago proveniente de un formulario.
 * 
 * Esta implementación de {@link PaymentProofStrategy} se encarga de generar
 * una entidad {@link PaymentProof} a partir de los datos enviados mediante el
 * DTO,
 * utilizando el servicio {@code PaymentProofService}, y la asocia al
 * participante.
 * 
 * Se utiliza cuando el comprobante de pago se proporciona como datos
 * estructurados
 * (no como archivo) desde un formulario.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-08
 */
@Component
@RequiredArgsConstructor
public class FormPaymentProofStrategy implements PaymentProofStrategy {

    private final PaymentProofService paymentProofService;

    /**
     * Procesa y asocia un comprobante de pago en formato formulario al
     * participante.
     * 
     * Utiliza {@link PaymentProofService} para crear la entidad
     * {@link PaymentProof}
     * a partir de los datos proporcionados en el DTO y la asigna al participante.
     * 
     * @param participant participante al que se asociará el comprobante de pago
     * @param dto         objeto con los datos del participante, incluyendo el
     *                    comprobante de pago
     */
    @Override
    public void process(Participant participant, CreateParticipantInternalDTO dto) {

        // mandamos a crear la prueba de pago y lo asignamos
        PaymentProof paymentProof = paymentProofService.createPaymentProof(dto.getPaymentProof(), participant);

        // le gurardamos al participante su prueba de pago y guardamos los cambios
        participant.setPaymentProof(paymentProof);
    }
}
