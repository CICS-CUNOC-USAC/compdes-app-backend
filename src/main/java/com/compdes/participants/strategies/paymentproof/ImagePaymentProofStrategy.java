package com.compdes.participants.strategies.paymentproof;

import org.springframework.stereotype.Component;

import com.compdes.participants.models.dto.internal.CreateParticipantInternalDTO;
import com.compdes.participants.models.entities.Participant;
import com.compdes.storedFiles.models.entities.StoredFile;
import com.compdes.storedFiles.services.StoredFileService;

import lombok.RequiredArgsConstructor;

/**
 * Estrategia para procesar la prueba de pago en formato de imagen.
 * 
 * Esta implementación de {@link PaymentProofStrategy} se encarga de almacenar
 * el archivo de imagen recibido como comprobante de pago utilizando el
 * servicio {@code StoredFileService}, y lo asocia al participante
 * correspondiente.
 * 
 * Se utiliza cuando el DTO recibido contiene un archivo de imagen como
 * prueba de pago.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-08
 */
@Component
@RequiredArgsConstructor
public class ImagePaymentProofStrategy implements PaymentProofStrategy {
    private final StoredFileService storedFileService;

    /**
     * Procesa y asocia una imagen como comprobante de pago al participante.
     * 
     * Almacena el archivo recibido utilizando {@code StoredFileService} y
     * lo asocia al participante. Se espera que el DTO contenga un archivo válido.
     * 
     * @param participant participante al que se asociará el comprobante de pago
     * @param dto         objeto con los datos del participante, incluyendo la
     *                    imagen
     *                    del comprobante de pago
     */
    @Override
    public void process(Participant participant, CreateParticipantInternalDTO dto) {
        // si viene una imagen de prueba de pago, la guardamos como imagen
        StoredFile storedFile = storedFileService.saveFile(dto.getPaymentProofImageMultipartFile());
        // le adjuntamos el archivo guardado a nuestro participante
        participant.setPaymentProofImage(storedFile);
    }
}
