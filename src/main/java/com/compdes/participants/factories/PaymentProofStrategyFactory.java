package com.compdes.participants.factories;

import org.springframework.stereotype.Component;

import com.compdes.participants.models.dto.internal.CreateParticipantInternalDTO;
import com.compdes.participants.strategies.paymentproof.FormPaymentProofStrategy;
import com.compdes.participants.strategies.paymentproof.ImagePaymentProofStrategy;
import com.compdes.participants.strategies.paymentproof.PaymentProofStrategy;

import lombok.RequiredArgsConstructor;

/**
 * Fábrica de estrategias para el procesamiento de comprobantes de pago.
 * 
 * Esta clase determina, en tiempo de ejecución, qué estrategia debe utilizarse
 * para procesar la prueba de pago proporcionada en un
 * {@link CreateParticipantInternalDTO},
 * delegando el manejo a la estrategia correspondiente según el tipo de entrada:
 * formulario estructurado o archivo de imagen.
 * 
 * Es utilizada en el flujo de creación de participantes para desacoplar la
 * lógica
 * de persistencia del comprobante según su origen.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-08
 */
@Component
@RequiredArgsConstructor
public class PaymentProofStrategyFactory {

    private final FormPaymentProofStrategy formStrategy;
    private final ImagePaymentProofStrategy imageStrategy;

    /**
     * Resuelve la estrategia adecuada para procesar el comprobante de pago del
     * participante.
     * 
     * Evalúa el contenido del DTO para determinar si se debe utilizar la estrategia
     * basada
     * en formulario estructurado o en imagen. Si no se proporciona ninguna forma de
     * comprobante
     * válida, lanza una excepción.
     * 
     * @param dto DTO con los datos del participante y del comprobante de pago
     * @return la estrategia que debe utilizarse para procesar el comprobante
     * @throws IllegalArgumentException si no se proporciona ninguna forma válida de
     *                                  comprobante
     */
    public PaymentProofStrategy resolve(CreateParticipantInternalDTO dto) {
        // si se proporcionó un formulario de comprobante de pago, usar la estrategia
        // basada en formulario
        if (dto.getPaymentProof() != null) {
            return formStrategy;
        }
        // si se proporcionó un archivo de imagen no vacío, usar la estrategia basada en
        // imagen
        else if (dto.getPaymentProofImageMultipartFile() != null &&
                !dto.getPaymentProofImageMultipartFile().isEmpty()) {
            return imageStrategy;
        }
        // si no se proporcionó ninguna forma de comprobante, lanzar excepción
        else {
            throw new IllegalArgumentException("No se proporcionó ninguna prueba de pago válida.");
        }
    }
}
