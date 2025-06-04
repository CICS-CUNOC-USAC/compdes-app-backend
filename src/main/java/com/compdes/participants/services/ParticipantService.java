package com.compdes.participants.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compdes.common.exceptions.DuplicateResourceException;
import com.compdes.common.exceptions.FileStorageException;
import com.compdes.participants.mappers.ParticipantMapper;
import com.compdes.participants.models.dto.internal.CreateNonAuthorParticipantInternalDTO;
import com.compdes.participants.models.dto.request.CreateAuthorParticipantDTO;
import com.compdes.participants.models.dto.request.CreateParticipantByAdminDTO;
import com.compdes.participants.models.dto.request.CreateParticipantDTO;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.repositories.ParticipantRepository;
import com.compdes.paymentProofs.models.entities.PaymentProof;
import com.compdes.paymentProofs.services.PaymentProofService;
import com.compdes.registrationStatus.models.entities.RegistrationStatus;
import com.compdes.registrationStatus.services.RegistrationStatusService;
import com.compdes.storedFiles.models.entities.StoredFile;
import com.compdes.storedFiles.services.StoredFileService;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de gestionar el CRUD de participantes
 * del sistema.
 * 
 * Administra tanto participantes autores como no autores, validando duplicidad
 * de datos,
 * asignando estado de registro y, en caso de participantes no autores,
 * asociando comprobantes de pago.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-30
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantMapper participantMapper;
    private final ParticipantRepository participantRepository;
    private final PaymentProofService paymentProofService;
    private final RegistrationStatusService registrationStatusService;
    private final StoredFileService storedFileService;

    /**
     * Obtiene la lista de todos los participantes registrados en el sistema.
     *
     * @return lista de participantes
     */
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    /**
     * Crea un participante no autor, guarda sus datos y asocia una prueba de pago.
     * 
     * Se espera que el participante proporcione una prueba de pago ya sea como
     * formulario
     * (link u objeto estructurado) o como imagen cargada. Si no se proporciona
     * ninguna
     * prueba de pago válida, se lanza una excepción.
     *
     * @param createParticipantDTO DTO con los datos del participante y su
     *                             comprobante de pago
     * @return el participante guardado con su comprobante asociado
     * @throws IllegalArgumentException   si no se proporciona ninguna prueba de
     *                                    pago
     *                                    o si la extensión del archivo de imagen no
     *                                    es válida
     * @throws FileStorageException       si ocurre un error al guardar el archivo
     *                                    en
     *                                    disco
     * @throws DuplicateResourceException si ya existe un participante con el mismo
     *                                    correo
     *                                    o documento de identificación
     */
    public Participant createNonAuthorParticipant(CreateNonAuthorParticipantInternalDTO createParticipantDTO) {

        // mandar a guardar al participante
        Participant savedParticipant = saveGenericParticipantOlineMethod(createParticipantDTO, false, false, null);

        // decidimos si guardar el comprobante de pago como link o como imagen
        // dependiendo que objeto venga nulo en el DTO
        if (createParticipantDTO.getPaymentProof() != null) {

            // mandamos a crear la prueba de pago y lo asignamos
            PaymentProof paymentProof = paymentProofService.createPaymentProof(createParticipantDTO.getPaymentProof(),
                    savedParticipant);

            // le gurardamosF al participante su pruba de pago y guardamos los cambios
            savedParticipant.setPaymentProof(paymentProof);

        } else if (createParticipantDTO.getPaymentProofImageMultipartFile() != null
                && !createParticipantDTO.getPaymentProofImageMultipartFile().isEmpty()) {

            // si viene una imagen de prueba de pago, la guardamos como imagen
            StoredFile storedFile = storedFileService
                    .saveFile(createParticipantDTO.getPaymentProofImageMultipartFile());

            // se adjuntamos el archivo guardado a nuestro participante
            savedParticipant.setPaymentProofImage(storedFile);
        } else {
            throw new IllegalArgumentException(
                    "No se proporcionó ninguna prueba de pago: debe enviarse un formulario o una imagen.");
        }

        savedParticipant = participantRepository.save(savedParticipant);
        return savedParticipant;

    }

    /**
     * Crea un participante (autor o no autor) desde el panel de administración.
     * 
     * Si el participante no es autor, se espera que proporcione un número de
     * comprobante
     * de pago, y se marca el tipo de pago como realizado en efectivo. En el caso de
     * autores,
     * no se requiere comprobante ni se marca pago.
     *
     * Este método delega el guardado final al método genérico
     * {@code saveGenericParticipantOlineMethod}.
     *
     * @param createParticipantByAdminDTO DTO con la información del participante a
     *                                    crear
     * @return el participante creado y persistido en base de datos
     *
     * @throws DuplicateResourceException si ya existe un participante con el mismo
     *                                    correo
     *                                    o documento de identificación
     */
    public Participant createParticipantByAdmin(CreateParticipantByAdminDTO createParticipantByAdminDTO) {

        String voucherNumber = null;
        Boolean isCashPayment = false;

        // si el participante no es un autor entonces se necestia guardar su comprobante
        // de pago e indicar que el pago se hizo en efectivo
        if (!createParticipantByAdminDTO.getIsAuthor()) {
            voucherNumber = createParticipantByAdminDTO.getVoucherNumber();
            isCashPayment = true;
        }

        // ahora que ya esta configurado todo podemos mandar a guardar
        Participant savedParticipant = saveGenericParticipantOlineMethod(createParticipantByAdminDTO,
                createParticipantByAdminDTO.getIsAuthor(),
                isCashPayment, voucherNumber);

        return savedParticipant;
    }

    /**
     * Crea un participante autor desde el formulario público (no administrador).
     * 
     * Dado que los autores no deben realizar pago, este método no asocia
     * ningún comprobante ni tipo de pago al participante.
     *
     * Se delega el guardado al método genérico
     * {@code saveGenericParticipantOlineMethod}.
     *
     * @param createParticipantDTO DTO con los datos del autor a registrar
     * @return el participante autor creado y guardado en base de datos
     *
     * @throws DuplicateResourceException si ya existe un participante con el mismo
     *                                    correo
     *                                    o documento de identificación
     */
    public Participant createAuthorParticipant(CreateAuthorParticipantDTO createParticipantDTO) {
        // mandar a guardar al participante, mandmaos null en el tipo de pago ya que un
        // autor no pagaS
        Participant savedParticipant = saveGenericParticipantOlineMethod(createParticipantDTO, true, null, null);
        return savedParticipant;
    }

    /**
     * Guarda un nuevo participante en el sistema y le asigna su estado de registro.
     * 
     *
     * @param createParticipantDTO DTO con los datos del participante
     * @param isAuthor             indica si el participante es autor
     * @param isCashPayment        indica si el pago fue realizado en efectivo
     *                             (puede ser null para autores)
     * @param voucherNumer         número de comprobante, si corresponde (puede ser
     *                             null)
     * @return el participante guardado con su estado de registro asignado
     *
     * @throws DuplicateResourceException si ya existe un participante con el mismo
     *                                    correo
     *                                    o documento de identificación
     */
    private Participant saveGenericParticipantOlineMethod(CreateParticipantDTO createParticipantDTO, Boolean isAuthor,
            Boolean isCashPayment, String voucherNumer) throws DuplicateResourceException {

        Participant participant = participantMapper.createParticipantDtoToParticipant(createParticipantDTO);
        participant.setIsAuthor(isAuthor);// le seteamos si es un autor o no

        // verificar que no exista otro participante con el mismo email
        if (participantRepository.existsByEmail(participant.getEmail())) {
            throw new DuplicateResourceException(
                    "No se puede completar el registro: el correo ingresado ya está asociado a otro participante.");
        }

        // verificar que no exista otro participante con el mismo doc de identificacion
        if (participantRepository.existsByIdentificationDocument(participant.getIdentificationDocument())) {
            throw new DuplicateResourceException(
                    "No se puede completar el registro: el documento de identificación ya está asociado a otro participante.");

        }

        // guardamos el participante para obtener un id y podr asociar una constancia de
        // pago
        participant = participantRepository.save(participant);

        // le creamos un estado de registro y lo guardamos
        RegistrationStatus registrationStatus = new RegistrationStatus(participant, false, isCashPayment, voucherNumer);
        registrationStatus = registrationStatusService
                .createRegistrationStatus(registrationStatus, participant);

        // le seteamos al participante su estado de registro
        participant.setRegistrationStatus(registrationStatus);
        participant = participantRepository.save(participant);

        return participant;
    }

}
