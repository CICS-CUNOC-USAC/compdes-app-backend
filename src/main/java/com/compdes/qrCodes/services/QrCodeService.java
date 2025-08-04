package com.compdes.qrCodes.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compdes.common.exceptions.NotFoundException;
import com.compdes.common.exceptions.QrCodeException;
import com.compdes.common.exceptions.enums.QrCodeErrorEnum;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.repositories.ParticipantRepository;
import com.compdes.qrCodes.models.entities.QrCode;
import com.compdes.qrCodes.repositories.QrCodeRepository;
import com.compdes.qrCodes.utils.QrCodeImageGeneratorUtil;

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
@Transactional(rollbackFor = Exception.class)
public class QrCodeService {

    private final QrCodeRepository qrCodeRepository;
    private final QrCodeImageGeneratorUtil qrImageGenerator;
    private final ParticipantRepository participantRepository;

    /**
     * Genera y guarda un nuevo código QR con un número incremental.
     * 
     * Este método obtiene el último código QR existente en la base de datos,
     * ordenado
     * por el campo {@code numberCode} de forma descendente. Si no existe ningún
     * registro,
     * comienza desde 1. De lo contrario, incrementa el valor más alto encontrado en
     * uno.
     * 
     * No está diseñado para ser expuesto como un endpoint HTTP, sino para uso
     * interno del sistema.
     * 
     * @return el nuevo código QR generado y persistido
     */
    public QrCode createQrCode() {
        // mandamos a trer el ultimo qr creado en base a su codigo numerico
        Optional<QrCode> optionalQrCode = qrCodeRepository.findFirstByOrderByNumberCodeDesc();
        // Si no existe, empezamos desde 1; si sí, incrementamos el valor
        int newNumberCode = optionalQrCode.map(qr -> qr.getNumberCode() + 1).orElse(1);

        // cramos el nuevo QR
        QrCode qrCode = new QrCode(null, newNumberCode);
        return qrCodeRepository.save(qrCode);
    }

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
        return qrCodeRepository.findFirstByParticipantIsNullOrderByNumberCodeAsc().orElseThrow(
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

    /**
     * Genera la imagen del código QR (formato PNG) a partir de su ID.
     * 
     * @param qrId identificador del código QR
     * @return arreglo de bytes que representa la imagen PNG del código QR generado
     * @throws NotFoundException     si no se encuentra un código QR con el ID
     *                               especificado
     * @throws IllegalStateException si el código QR no está vinculado a ningún
     *                               participante
     */
    public byte[] getQrImageByQrId(String qrId) throws NotFoundException {
        QrCode qrCode = getQrCodeById(qrId);
        validateQrCodeHasParticipant(qrCode);
        return qrImageGenerator.generateQrCode(qrCode);
    }

    /**
     * Obtiene la imagen del código QR en formato PNG asociada al usuario indicado.
     * 
     * @param username nombre de usuario del participante
     * @return arreglo de bytes con la imagen PNG del código QR
     * @throws NotFoundException     si no se encuentra un código QR asociado al
     *                               usuario
     * @throws IllegalStateException si el código QR no está vinculado a un
     *                               participante
     */
    public byte[] getQrImageByQrUsername(String username) throws NotFoundException {
        QrCode qrCode = getQrCodeByUsername(username);
        return qrImageGenerator.generateQrCode(qrCode);
    }

    /**
     * Recupera un código QR por su ID.
     * 
     * @param id identificador del código QR
     * @return el código QR correspondiente
     * @throws NotFoundException si no se encuentra un código QR con el ID dado
     */
    private QrCode getQrCodeById(String id) throws NotFoundException {
        return qrCodeRepository.findById(id).orElseThrow(() -> new NotFoundException("No se encontró ningún código QR. "
                + "Si estás seguro de que el código QR existe o te pertenece, contacta al equipo de soporte."));
    }

    /**
     * Obtiene un código QR vinculado a un nombre de usuario.
     * 
     * @param username nombre de usuario del participante
     * @return código QR asociado
     * @throws NotFoundException si no se encuentra un código QR vinculado al
     *                           usuario
     */
    private QrCode getQrCodeByUsername(String username) throws NotFoundException {
        return qrCodeRepository.findByParticipant_CompdesUser_Username(username)
                .orElseThrow(() -> new NotFoundException("No pudimos encontrar un código QR vinculado a tu usuario. "
                        + "Si estás seguro de que ya completaste tu inscripción o este QR te pertenece, por favor contacta al equipo de soporte para ayudarte."));
    }

    /**
     * Valida que el código QR tenga un participante asociado.
     * 
     * @param qrCode código QR a validar
     * @throws IllegalStateException si el código QR no tiene un participante
     *                               asociado
     */
    private void validateQrCodeHasParticipant(QrCode qrCode) {
        if (qrCode.getParticipant() == null) {
            throw new IllegalStateException("Parece que este código QR aún no está vinculado a ningún participante. "
                    + "Si ya completaste tu inscripción y crees que este es tu código, por favor contacta al equipo de soporte para ayudarte.");
        }
    }

    public Long count() {
        return qrCodeRepository.count();
    }
}
