package com.compdes.paymentProofs.services;

import org.springframework.stereotype.Service;

import com.compdes.common.exceptions.NotFoundException;
import com.compdes.participants.models.entities.Participant;
import com.compdes.paymentProofs.mappers.PaymentProofMapper;
import com.compdes.paymentProofs.models.dto.request.CreatePaymentProofDTO;
import com.compdes.paymentProofs.models.dto.request.UpdatePaymentProofByAdminDTO;
import com.compdes.paymentProofs.models.entities.PaymentProof;
import com.compdes.paymentProofs.repositories.PaymentProofRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de la gestión de comprobantes de pago.
 * 
 * Esta clase proporciona la lógica de negocio necesaria para manejar los
 * comprobantes de pago asociados a participantes.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-30
 */
@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class PaymentProofService {

    private final PaymentProofMapper paymentProofMapper;
    private final PaymentProofRepository paymentProofRepository;

    /**
     * Crea y guarda un nuevo comprobante de pago asociado a un participante.
     * 
     * Este método se encarga de mapear el DTO de entrada a una entidad
     * {@link PaymentProof},
     * asignarle el participante correspondiente, y persistirlo en la base de datos.
     * 
     * @param createPaymentProofDTO DTO que contiene la información del comprobante
     *                              de pago
     * @param participant           participante al que se asociará el comprobante
     *                              de pago
     * @return el comprobante de pago guardado en la base de datos
     */
    public PaymentProof createPaymentProof(CreatePaymentProofDTO createPaymentProofDTO,
            Participant participant) {
        // mapearmos el dt
        PaymentProof paymentProof = paymentProofMapper
                .createPaymentProofToPaymentProof(createPaymentProofDTO);
        // asignamos el participante
        paymentProof.setParticipant(participant);
        // guardamos el comprobante de pago
        PaymentProof save = paymentProofRepository.save(paymentProof);

        return save;
    }

    /**
     * Actualiza el enlace del comprobante de pago de un participante desde el panel
     * de administración.
     * 
     * @param paymentProof el comprobante de pago existente asociado al participante
     * @param dto          objeto que contiene el nuevo enlace del comprobante de
     *                     pago
     * @return el comprobante de pago actualizado
     * @throws IllegalArgumentException si el comprobante de pago es {@code null}
     *                                  (por ejemplo, si el participante no pagó con
     *                                  tarjeta)
     */
    public PaymentProof updatePaymentProof(PaymentProof paymentProof, UpdatePaymentProofByAdminDTO dto)
            throws NotFoundException {
        if (paymentProof == null) {
            throw new IllegalArgumentException(
                    "Parece que este participante no realizó un pago con tarjeta en la app Recurrente, por lo tanto, no se le puede asignar un número de comprobante.");
        }
        paymentProof.setLink(dto.getLink());
        return paymentProofRepository.save(paymentProof);
    }

    public PaymentProof getPaymentProofById(String id) throws NotFoundException {
        return paymentProofRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("No se encontró un comprobante de pago con el ID proporcionado."));
    }

}
