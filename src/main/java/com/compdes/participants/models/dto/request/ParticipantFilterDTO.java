package com.compdes.participants.models.dto.request;

import lombok.Value;

/**
 * 
 * DTO utilizado para aplicar filtros en la búsqueda de participantes.
 * 
 * Esta clase encapsula los posibles criterios de filtrado que se pueden aplicar
 * al consultar registros de participantes.
 * 
 * Todos los campos son opcionales y permiten construir consultas dinámicas en
 * función
 * de los parámetros provistos.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-13
 */
@Value
public class ParticipantFilterDTO {

    String firstName;
    String lastName;
    String email;
    String phone;
    String organisation;
    String identificationDocument;

    Boolean isAuthor;
    Boolean isGuest;

    Boolean isTransferPayment;
    Boolean isCardPayment;
    Boolean isCashPayment;

    Boolean isApproved;
    String voucherNumber;
}
