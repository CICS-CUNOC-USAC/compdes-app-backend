package com.compdes.participants.models.dto.internal;

import org.springframework.web.multipart.MultipartFile;

import com.compdes.participants.models.dto.request.CreateParticipantDTO;
import com.compdes.paymentProofs.models.dto.request.CreatePaymentProofDTO;

import lombok.Getter;

/**
 * DTO interno utilizado para el registro de participantes no autores.
 *
 * Este DTO extiende de {@link CreateParticipantDTO} para reutilizar los datos
 * comunes de un participante, y añade información adicional necesaria solo para
 * participantes no autores, como el comprobante de pago y su imagen asociada.
 *
 * Este objeto se utiliza exclusivamente entre capas del sistema y no está
 * diseñado
 * para ser expuesto directamente en la API pública.
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-03
 */
@Getter
public class CreateNonAuthorParticipantInternalDTO extends CreateParticipantDTO {

    CreatePaymentProofDTO paymentProof;
    MultipartFile paymentProofImageMultipartFile;

    public CreateNonAuthorParticipantInternalDTO(String firstName, String lastName, String email, String phone,
            String organisation, String identificationDocument, CreatePaymentProofDTO paymentProof,
            MultipartFile paymentProofImageMultipartFile) {
        super(firstName, lastName, email, phone, organisation, identificationDocument);
        this.paymentProof = paymentProof;
        this.paymentProofImageMultipartFile = paymentProofImageMultipartFile;
    }

    public void setPaymentProofImage(MultipartFile paymentProofImageMultipartFile) {
        this.paymentProofImageMultipartFile = paymentProofImageMultipartFile;
    }

}