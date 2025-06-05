package com.compdes.paymentProofs.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-30
 */
@Getter
@Setter
@AllArgsConstructor
public class CreatePaymentProofDTO {

    @NotBlank(message = "Debe proporcionar un enlace al comprobante de pago")
    @Size(max = 100, message = "El enlace no puede exceder los 100 caracteres")
    // @Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$", message = "El
    // enlace debe tener un formato de URL válido")
    private String link;
}
