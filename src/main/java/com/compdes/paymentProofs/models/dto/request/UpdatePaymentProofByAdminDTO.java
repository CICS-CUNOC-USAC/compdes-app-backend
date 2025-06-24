package com.compdes.paymentProofs.models.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

/**
 * DTO utilizado para actualizar el comprobante de pago de un participante por
 * parte de un administrador, aceptando cualquier link https o http
 * 
 * Este DTO se emplea en operaciones donde un administrador desea registrar o
 * corregir
 * manualmente el enlace al comprobante de pago de un participante.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-22
 */
@Value
public class UpdatePaymentProofByAdminDTO {

    @NotBlank(message = "Debe proporcionar un enlace al comprobante de pago")
    @Size(max = 100, message = "El enlace no puede exceder los 100 caracteres")
    @Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$", message = "Debe proporcionar un enlace válido (http o https)")
    @Schema(description = "Enlace al comprobante de pago. Se acepta cualquier tipo de link, ya que está diseñado únicamente para uso administrativo.")
    private String link;
}
