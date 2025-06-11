package com.compdes.participants.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compdes.auth.users.models.entities.CompdesUser;
import com.compdes.common.exceptions.DuplicateResourceException;
import com.compdes.common.exceptions.NotFoundException;
import com.compdes.participants.mappers.ParticipantMapper;
import com.compdes.participants.models.dto.internal.CreateParticipantInternalDTO;
import com.compdes.participants.models.dto.request.CreateParticipantByAdminDTO;
import com.compdes.participants.models.dto.request.CreateParticipantDTO;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.repositories.ParticipantRepository;
import com.compdes.participants.strategies.paymentproof.PaymentProofStrategyFactory;
import com.compdes.qrCodes.models.entities.QrCode;
import com.compdes.qrCodes.services.QrCodeService;
import com.compdes.registrationStatus.models.entities.RegistrationStatus;
import com.compdes.registrationStatus.services.RegistrationStatusService;

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
        private final QrCodeService qrCodeService;

        /**
         * Obtiene la lista de todos los participantes registrados en el sistema.
         *
         * @return lista de participantes
         */
        public List<Participant> getAllParticipants() {
                return participantRepository.findAll();
        }

        /**
         * Busca un participante en la base de datos utilizando su identificador único.
         * 
         * Este método intenta recuperar un {@link Participant} por su ID. Si no se
         * encuentra
         * ningún registro correspondiente, lanza una excepción indicando que el
         * participante no existe.
         * 
         * @param id identificador único del participante a buscar
         * @return el participante correspondiente al ID proporcionado
         * @throws NotFoundException si no se encuentra ningún participante con el ID
         *                           especificado
         */
        public Participant findParticipantById(String id) throws NotFoundException {
                return participantRepository.findById(id).orElseThrow(
                                () -> new NotFoundException(
                                                "No se encontró un participante con el ID proporcionado. Por favor, verifica la información e intenta nuevamente."));
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
                RegistrationStatus registrationStatus = RegistrationStatus.builder()
                                .isApproved(false)
                                .isCashPayment(false)
                                .voucherNumber(null)
                                .build();

                // mandar a guardar al participante
                Participant savedParticipant = saveGenericParticipant(createParticipantDTO,
                                registrationStatus, false);

                // aplicar una estrategia
                paymentProofStrategyFactory
                                .resolve(createParticipantDTO)
                                .process(savedParticipant, createParticipantDTO);

                return participantRepository.save(savedParticipant);// se vuelve a guardar porque las estrategias
                                                                    // generan
                                                                    // relaciones
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
         */
        public Participant createParticipantByAdmin(CreateParticipantByAdminDTO createParticipantByAdminDTO) {
                // se construye un nuevo estado de registro para el participante
                RegistrationStatus registrationStatus = RegistrationStatus.builder()
                                .isApproved(false) // se marca como no aprobado por defecto
                                // se indica si el pago fue en efectivo solo si no es invitado (invitado no
                                // paga)
                                .isCashPayment(createParticipantByAdminDTO.getIsGuest() ? null : true)
                                // si el participante es invitado, no se asigna número de comprobante; de lo
                                // contrario, si
                                .voucherNumber(createParticipantByAdminDTO.getIsGuest() ? null
                                                : createParticipantByAdminDTO.getVoucherNumber())
                                .build();

                // ahora que ya esta configurado todo podemos mandar a guardar
                Participant savedParticipant = saveGenericParticipant(createParticipantByAdminDTO,
                                registrationStatus, createParticipantByAdminDTO.getIsGuest());

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
        private Participant saveGenericParticipant(CreateParticipantDTO createParticipantDTO,
                        RegistrationStatus registrationStatus, Boolean isGuest) throws DuplicateResourceException {

                Participant participant = participantMapper.createParticipantDtoToParticipant(createParticipantDTO);
                participant.setIsGuest(isGuest);

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
                // guardamos el estado del registro
                registrationStatus = registrationStatusService
                                .createRegistrationStatus(registrationStatus, participant);

                // le seteamos al participante su estado de registro
                participant.setRegistrationStatus(registrationStatus);

                return participantRepository.save(participant);
        }

        /**
         * Asocia un usuario del sistema a un participante y persiste la relación en la
         * base de datos.
         * 
         * Este método asigna la instancia de {@link CompdesUser} al participante dado,
         * guarda
         * la entidad actualizada en la base de datos y retorna el participante
         * resultante.
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
         * Este método obtiene un código QR no asignado mediante el servicio
         * {@link QrCodeService}, establece la relación entre el participante y el
         * código QR, y guarda los cambios en la base de datos.
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

}
