package com.compdes.participants.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compdes.auth.users.models.entities.CompdesUser;
import com.compdes.common.exceptions.DuplicateResourceException;
import com.compdes.common.exceptions.NotFoundException;
import com.compdes.participants.enums.ParticipantErrorMessages;
import com.compdes.participants.factories.PaymentProofStrategyFactory;
import com.compdes.participants.mappers.ParticipantMapper;
import com.compdes.participants.models.dto.internal.CreateParticipantInternalDTO;
import com.compdes.participants.models.dto.request.CreateParticipantByAdminDTO;
import com.compdes.participants.models.dto.request.CreateParticipantDTO;
import com.compdes.participants.models.dto.request.ParticipantFilterDTO;
import com.compdes.participants.models.dto.request.UpdateParticipantByAdminDTO;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.repositories.ParticipantRepository;
import com.compdes.participants.repositories.specifications.ParticipantSpecification;
import com.compdes.paymentProofs.services.PaymentProofService;
import com.compdes.qrCodes.models.entities.QrCode;
import com.compdes.qrCodes.repositories.QrCodeRepository;
import com.compdes.qrCodes.services.QrCodeService;
import com.compdes.registrationStatus.factories.RegistrationStatusFactory;
import com.compdes.registrationStatus.models.entities.RegistrationStatus;
import com.compdes.registrationStatus.services.RegistrationStatusService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
        private final PaymentProofStrategyFactory paymentProofStrategyFactory;
        private final RegistrationStatusService registrationStatusService;
        private final PaymentProofService paymentProofService;
        private final RegistrationStatusFactory registrationStatusFactory;
        private final ParticipantValidationService participantValidationService;
        private final QrCodeService qrCodeService;

        public Participant saveParticipant(Participant participant) {
                return participantRepository.save(participant);
        }

        /**
         * Obtiene la lista de paginada y filtrada con los participantes registrados en
         * el sistema.
         *
         * @return lista de participantes
         */
        public Page<Participant> getAllParticipants(ParticipantFilterDTO filters, Pageable pageable) {
                Specification<Participant> spec = Specification.allOf(
                                ParticipantSpecification.filterBy(filters));
                return participantRepository.findAll(spec, pageable);
        }

        /**
         * Obtiene la lista filtrada de los participantes registrados en el sistema.
         * 
         * @return Lista con todos los participantes en la db
         */
        public List<Participant> getAllParticipants(ParticipantFilterDTO filters) {
                Specification<Participant> spec = Specification.allOf(
                                ParticipantSpecification.filterBy(filters));
                return participantRepository.findAll(spec);
        }

        /**
         * Recupera un participante utilizando su documento de identificación.
         * 
         * @param identificationDocument documento de identificación del participante
         * @return el participante correspondiente al documento proporcionado
         * @throws NotFoundException si no se encuentra ninguna inscripción con el
         *                           documento dado.
         */
        public Participant getParticipantByIdentificationDocument(String identificationDocument)
                        throws NotFoundException {
                return participantRepository.findByIdentificationDocument(identificationDocument).orElseThrow(
                                () -> new NotFoundException(
                                                ParticipantErrorMessages.NOT_FOUND_BY_DOCUMENT.getMessage()));

        }

        /**
         * Obtiene un participante a partir del ID de su código QR.
         * 
         * @param id el identificador del código QR
         * @return el participante asociado al código QR
         * @throws NotFoundException si no se encuentra ningún participante con ese
         *                           código QR
         */
        public Participant getParticipantByQrCodeId(String id) throws NotFoundException {
                return participantRepository.findByQrCode_Id(id).orElseThrow(
                                () -> new NotFoundException(ParticipantErrorMessages.NOT_FOUND_BY_QR.getMessage()));
        }

        /**
         * Obtiene un participante a partir del nombre de usuario.
         * 
         * @param username nombre de usuario del sistema COMPDES
         * @return el participante asociado al nombre de usuario proporcionado
         * @throws NotFoundException si no existe ningún participante vinculado al
         *                           usuario ingresado
         */
        public Participant getParticipantByUserName(String username)
                        throws NotFoundException {
                return participantRepository.findByCompdesUser_Username(username).orElseThrow(
                                () -> new NotFoundException(
                                                ParticipantErrorMessages.NOT_FOUND_BY_USERNAME.getMessage()));
        }

        /**
         * Busca un participante en la base de datos utilizando su identificador único.
         * 
         * @param id identificador único del participante a buscar
         * @return el participante correspondiente al ID proporcionado
         * @throws NotFoundException si no se encuentra ningún participante con el ID
         *                           especificado
         */
        public Participant getParticipantById(String id) throws NotFoundException {
                return participantRepository.findById(id).orElseThrow(
                                () -> new NotFoundException(ParticipantErrorMessages.NOT_FOUND_BY_ID.getMessage()));
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
         */
        public Participant createParticipant(CreateParticipantInternalDTO createParticipantDTO) {
                // obtiene una instancia de RegistrationStatus
                RegistrationStatus registrationStatus = registrationStatusFactory.createDefaultForPublic();

                Participant savedParticipant = saveGenericParticipant(createParticipantDTO,
                                registrationStatus, false);// guarda el participante para obtener un id

                paymentProofStrategyFactory
                                .resolve(createParticipantDTO)
                                .process(savedParticipant, createParticipantDTO);// aplica una estrategia

                return participantRepository.save(savedParticipant);// guarda las estrategias
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
         * @param createParticipantByAdminDTO DTO con la información del participante a
         *                                    crear
         * @return el participante creado y persistido en base de datos
         */
        public Participant createParticipantByAdmin(CreateParticipantByAdminDTO createParticipantByAdminDTO) {
                // se construye un nuevo estado de registro para el participante
                RegistrationStatus registrationStatus = registrationStatusFactory.fromAdminInput(
                                createParticipantByAdminDTO.getIsGuest(),
                                createParticipantByAdminDTO.getVoucherNumber());
                // ahora que ya esta configurado todo podemos mandar a guardar
                Participant savedParticipant = saveGenericParticipant(createParticipantByAdminDTO,
                                registrationStatus, createParticipantByAdminDTO.getIsGuest());
                // madamos a aprovar el registro
                registrationStatusService.approveRegistrationByParticipant(savedParticipant);
                return savedParticipant;
        }

        /**
         * Actualiza los datos de un participante desde el panel de administración.
         * 
         * Si se incluye un comprobante de pago o número de talonario, también se
         * actualizan.
         * 
         * @param participantId ID del participante a actualizar
         * @param dto           objeto con los nuevos datos
         * @return el participante actualizado
         * 
         * @throws NotFoundException          si no se encuentra el participante, el
         *                                    comprobante de pago o el estado de
         *                                    registro
         * @throws DuplicateResourceException si el correo, documento o número de
         *                                    talonario ya están en uso
         * @throws IllegalArgumentException   si el participante no cumple con las
         *                                    condiciones para actualizar pago o
         *                                    talonario
         */
        public Participant updateParticipantByAdmin(String participantId, UpdateParticipantByAdminDTO dto)
                        throws NotFoundException {
                Participant participant = getParticipantById(participantId);// trae el participante por id

                participantValidationService.validateUniqueDocumentExcludingId(dto.getIdentificationDocument(),
                                participantId, ParticipantErrorMessages.DUPLICATE_DOCUMENT.getMessage());

                participantValidationService.validateUniqueEmailExcludingId(dto.getEmail(),
                                participantId, ParticipantErrorMessages.DUPLICATE_EMAIL.getMessage());

                participant.update(dto);// guarda la actualizacion de la info personal

                if (dto.getPaymentProof() != null) { // actualia el payment si se envio
                        paymentProofService.updatePaymentProof(participant.getPaymentProof(), dto.getPaymentProof());
                }

                if (dto.getVoucherNumber() != null) {// actualiza el talonario si se envio
                        registrationStatusService.updateRegistrationStatus(participant.getRegistrationStatus().getId(),
                                        dto.getVoucherNumber());
                }

                return participantRepository.save(participant);
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
        private Participant saveGenericParticipant(CreateParticipantDTO createParticipantDTO,
                        RegistrationStatus registrationStatus, Boolean isGuest) throws DuplicateResourceException {

                Participant participant = participantMapper.createParticipantDtoToParticipant(createParticipantDTO);
                participant.setIsGuest(isGuest);

                participantValidationService.validateUniqueEmail(participant.getEmail(),
                                ParticipantErrorMessages.DUPLICATE_EMAIL.getMessage());

                participantValidationService.validateUniqueDocument(participant.getIdentificationDocument(),
                                ParticipantErrorMessages.DUPLICATE_DOCUMENT.getMessage());

                participant = participantRepository.save(participant);// guarda para obtener un id

                registrationStatus = registrationStatusService
                                .createRegistrationStatus(registrationStatus, participant);// guarda RegistrationStatus

                participant.setRegistrationStatus(registrationStatus);/// relaciona Participant-RegistrationStatus

                return participantRepository.save(participant);
        }

        /**
         * Asocia un usuario del sistema a un participante y persiste la relación en la
         * base de datos.
         * 
         * <strong>Nota:</strong> Este método forma parte de la lógica interna de la
         * aplicación y
         * no está diseñado para ser expuesto como un endpoint HTTP.
         * 
         * @param compdesUser usuario del sistema que se desea asociar al participante
         * @param participant entidad del participante al que se le asignará el usuario
         * @return el participante actualizado con el usuario asociado
         */
        public Participant setUserParticipant(CompdesUser compdesUser, Participant participant) {
                // Asigna el usuario proporcionado al participante
                participant.setCompdesUser(compdesUser);

                // Retorna el participante actualizado
                return participantRepository.save(participant);

        }

        /**
         * Asigna un código QR disponible a un participante y persiste la relación.
         * 
         * <strong>Nota:</strong> Este método forma parte de la lógica interna de la
         * aplicación y
         * no está diseñado para ser expuesto como un endpoint HTTP.
         * 
         * @param participant participante al que se asignará el código QR
         * @return el participante actualizado con el código QR asignado
         */
        public Participant assignQrToParticipant(Participant participant) {
                // mandamos a traer un codigo Qr vacio al usuario
                QrCode qrCode = qrCodeService.findAvailableQrCode();

                // asignamos el participante al qr
                qrCode = qrCodeService.assignParticipantToQrCode(participant, qrCode);

                // le asignamos el qr al participante
                participant.setQrCode(qrCode);

                // Retorna el participante actualizado
                return participantRepository.save(participant);
        }

        /**
         * Elimina un participante del sistema si aún no ha sido confirmado.
         * 
         * @param id identificador del participante que se desea eliminar
         * @throws NotFoundException     si no se encuentra un participante con el ID
         *                               proporcionado
         * @throws IllegalStateException si el participante ya fue confirmado y no puede
         *                               ser eliminado
         */
        public void deleteParticipant(String id) throws NotFoundException {
                Participant participant = getParticipantById(id);

                if (participant.getRegistrationStatus().getIsApproved()) {// participante confirmado no puede borrarse
                        throw new IllegalStateException(
                                        ParticipantErrorMessages.PARTICIPANT_ALREADY_CONFIRMED.getMessage());
                }
                participantRepository.deleteById(id);
        }

        private final QrCodeRepository qrCodeRepository;
        @PersistenceContext
        EntityManager entityManager;

        public void reassignQrsToApprovedParticipants() {
                // 1. Obtener participantes aprobados
                List<Participant> participants = participantRepository
                                .findByRegistrationStatus_IsApprovedOrderByCreatedAtAsc(true);

                // 2. Obtener los códigos QR ordenados por número ascendente
                List<QrCode> qrCodes = qrCodeRepository.findAllByOrderByNumberCodeAsc();

                // 3. Validar que hay suficientes códigos
                if (qrCodes.size() < participants.size()) {
                        throw new IllegalStateException("No hay suficientes códigos QR disponibles para reasignar.");
                }

                participants.forEach(participant -> participant.setQrCodeWithoutExeption(null));
                // 4. Desvincular los QR anteriores (opcional)
                qrCodes.forEach(qr -> qr.setParticipantWithoutExeption(null));

                participantRepository.saveAll(participants);
                qrCodeRepository.saveAll(qrCodes);

                entityManager.flush();

                // 5. Asignar los nuevos QRs
                for (int i = 0; i < participants.size(); i++) {
                        Participant participant = participants.get(i);
                        QrCode qrCode = qrCodes.get(i);
                        participant.setQrCodeWithoutExeption(qrCode);
                        qrCode.setParticipantWithoutExeption(participant); // esto validará si ya estaba asignado
                }

                // 6. Guardar cambios
                participantRepository.saveAll(participants);
                qrCodeRepository.saveAll(qrCodes);
        }
}
