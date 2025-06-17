package com.compdes.paymentProofs.mappers;

import org.mapstruct.Mapper;

import com.compdes.paymentProofs.models.dto.request.CreatePaymentProofDTO;
import com.compdes.paymentProofs.models.entities.PaymentProof;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-30
 */
@Mapper(componentModel = "spring")
public interface PaymentProofMapper {

    /**
     * Convierte un DTO de creación de constancia de pago a una entidad
     * `PaymentProof`.
     * 
     * Este método mapea únicamente los datos relevantes desde el DTO,
     * ignorando explícitamente las propiedades que deben ser generadas o asignadas
     * manualmente desde la lógica de negocio.
     * 
     * <p>
     * <strong>Propiedades ignoradas durante el mapeo:</strong>
     * </p>
     * <ul>
     * <li><code>id</code>: generado automáticamente al persistir la entidad</li>
     * <li><code>createdAt</code>, <code>updatedAt</code>, <code>deletedAt</code>,
     * <code>desactivatedAt</code>:
     * manejadas por herencia desde <code>Auditor</code></li>
     * <li><code>participant</code>: debe establecerse explícitamente en la capa de
     * servicio</li>
     * </ul>
     *
     * @param createPaymentProof DTO que contiene el enlace al documento de
     *                           constancia de pago
     * @return una instancia parcial de `PaymentProof` con los datos mapeados desde
     *         el DTO
     */
    public PaymentProof createPaymentProofToPaymentProof(CreatePaymentProofDTO createPaymentProof);

}
