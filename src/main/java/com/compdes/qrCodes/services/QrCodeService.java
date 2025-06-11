package com.compdes.qrCodes.services;

import org.springframework.stereotype.Service;

import com.compdes.common.exceptions.QrCodeException;
import com.compdes.common.exceptions.enums.QrCodeErrorEnum;
import com.compdes.participants.models.entities.Participant;
import com.compdes.qrCodes.models.entities.QrCode;
import com.compdes.qrCodes.repositories.QrCodeRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de la gestión de códigos QR dentro del sistema.
 * 
 * Centraliza la lógica relacionada con la consulta, asignación, validación
 * y generación de códigos QR, garantizando la integridad de su uso
 * en el contexto de registro y control de acceso de participantes.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-06
 */
@Service
@RequiredArgsConstructor
public class QrCodeService {

    private final QrCodeRepository qrCodeRepository;

    /**
     * Busca y retorna un código QR disponible para asignación.
     * 
     * Este método consulta el repositorio para encontrar el primer código QR
     * cuya relación con un {@link Participant} sea nula, lo que indica que aún no
     * ha sido utilizado.
     * 
     * Si no se encuentra ningún código QR disponible, lanza una excepción del tipo
     * {@link QrCodeException}, definida en
     * {@link QrCodeErrorEnum#NO_AVAILABLE_QR_CODE}.
     * 
     * <strong>Nota:</strong> Este método forma parte de la lógica interna del
     * sistema
     * y no está diseñado para ser expuesto como un endpoint HTTP.
     * 
     * @return un código QR no asignado
     * @throws QrCodeException si no hay códigos QR disponibles para asignar
     */
    public QrCode findAvailableQrCode() {
        return qrCodeRepository.findFirstByParticipantIsNull().orElseThrow(
                () -> QrCodeErrorEnum.NO_AVAILABLE_QR_CODE.getQrCodeException());
    }

    /**
     * Asocia un participante a un código QR y persiste la relación en la base de
     * datos.
     * 
     * Este método utiliza el setter de {@link QrCode} para establecer la relación
     * con el {@link Participant}, y luego guarda el código QR actualizado en el
     * repositorio.
     * 
     * Si el código QR ya está asignado a otro participante, se lanza una excepción
     * del tipo {@link QrCodeException}, definida en
     * {@link QrCodeErrorEnum#QR_ALREADY_ASSIGNED}.
     * 
     * <strong>Nota:</strong> Este método forma parte de la lógica interna del
     * sistema y
     * no está diseñado para ser expuesto como un endpoint HTTP.
     * 
     * @param participant el participante que se desea asociar
     * @param qrCode      el código QR al que se le asignará el participante
     * @return el código QR actualizado con la relación persistida
     * @throws QrCodeException si el código QR ya está asignado a un participante
     */
    public QrCode assignParticipantToQrCode(Participant participant, QrCode qrCode) {
        qrCode.setParticipant(participant);
        return qrCodeRepository.save(qrCode);
    }

}
