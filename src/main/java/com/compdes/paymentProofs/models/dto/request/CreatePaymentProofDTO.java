package com.compdes.paymentProofs.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^https://app\\.recurrente\\.com/checkout-session/[^\\s]+$", message = "Solo se permiten enlaces válidos de la app Recurrente. Ejemplo: https://app.recurrente.com/checkout-session/ch_ejemplodummy123")
    private String link;
}
