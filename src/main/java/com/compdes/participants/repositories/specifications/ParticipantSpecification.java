package com.compdes.participants.repositories.specifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.compdes.participants.models.dto.request.ParticipantFilterDTO;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.models.entities.Participant_;
import com.compdes.registrationStatus.models.entities.RegistrationStatus;
import com.compdes.registrationStatus.models.entities.RegistrationStatus_;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

/**
 * Genera dinámicamente criterios de filtrado para la entidad
 * {@link Participant}.
 * 
 * Esta clase construye una {@link Specification} en función de los parámetros
 * contenidos en un {@link ParticipantFilterDTO}, permitiendo realizar búsquedas
 * flexibles por distintos atributos del participante, así como por campos
 * relacionados en {@link RegistrationStatus}.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-13
 */
public class ParticipantSpecification {

    /**
     * Construye dinámicamente una {@link Specification} para aplicar filtros
     * sobre la entidad {@link Participant}, con base en los campos no nulos del
     * {@link ParticipantFilterDTO} recibido.
     * 
     * <p>
     * Permite realizar búsquedas flexibles utilizando criterios parciales sobre
     * texto (como nombre, correo o teléfono), así como filtros exactos sobre
     * campos booleanos y relacionados, incluyendo atributos de
     * {@link RegistrationStatus}.
     * </p>
     * 
     * <p>
     * Si el parámetro {@code filter} es {@code null}, se retorna una
     * especificación sin restricciones, equivalente a un {@code WHERE true}.
     * </p>
     * 
     * @param filter objeto con los criterios de búsqueda a aplicar
     * @return una especificación que puede usarse en repositorios JPA
     */
    public static Specification<Participant> filterBy(ParticipantFilterDTO filter) {
        if (filter == null) {
            return (root, query, cb) -> cb.conjunction();// devuelve todos los registros
        }
        return (root, query, cb) -> {
            // Join con RegistrationStatus
            Join<Participant, RegistrationStatus> regStatusJoin = root.join(Participant_.registrationStatus,
                    JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();

            // Filtra por nombre (coincidencia parcial, sin distinción entre mayúsculas y
            // minúsculas)
            if (filter.getFirstName() != null) {
                predicates.add(cb.like(cb.lower(root.get(Participant_.firstName)),
                        "%" + filter.getFirstName().toLowerCase() + "%"));
            }

            // Filtra por apellido (coincidencia parcial, sin distinción entre mayúsculas y
            // minúsculas)
            if (filter.getLastName() != null) {
                predicates.add(cb.like(cb.lower(root.get(Participant_.lastName)),
                        "%" + filter.getLastName().toLowerCase() + "%"));
            }

            // Filtra por correo electrónico (coincidencia parcial, sin distinción entre
            // mayúsculas y minúsculas)
            if (filter.getEmail() != null) {
                predicates.add(cb.like(cb.lower(root.get(Participant_.email)),
                        "%" + filter.getEmail().toLowerCase() + "%"));
            }

            // Filtra por número de teléfono (coincidencia parcial, sin distinción entre
            // mayúsculas y minúsculas)
            if (filter.getPhone() != null) {
                predicates.add(
                        cb.like(cb.lower(root.get(Participant_.phone)), "%" + filter.getPhone().toLowerCase() + "%"));
            }

            // Filtra por organización o institución (coincidencia parcial, sin distinción
            // entre mayúsculas y minúsculas)
            if (filter.getOrganisation() != null) {
                predicates.add(
                        cb.like(cb.lower(root.get(Participant_.organisation)),
                                "%" + filter.getOrganisation().toLowerCase() + "%"));
            }

            // Filtra por documento de identificación (coincidencia parcial, sin distinción
            // entre mayúsculas y minúsculas)
            if (filter.getIdentificationDocument() != null) {
                predicates.add(
                        cb.like(cb.lower(root.get(Participant_.identificationDocument)),
                                "%" + filter.getIdentificationDocument().toLowerCase() + "%"));
            }

            // Filtra por participantes autores (true = es autor, false = no es autor)
            if (filter.getIsAuthor() != null) {
                predicates.add(cb.equal(root.get(Participant_.isAuthor), filter.getIsAuthor()));
            }

            // Filtra por participantes invitados (true = es invitado, false = no es
            // invitado)
            if (filter.getIsGuest() != null) {
                predicates.add(cb.equal(root.get(Participant_.isGuest), filter.getIsGuest()));
            }

            // Filtra por método de pago en efectivo (true = pagó en efectivo, false = otro
            // método)
            if (filter.getIsCashPayment() != null) {
                predicates
                        .add(cb.equal(regStatusJoin.get(RegistrationStatus_.isCashPayment), filter.getIsCashPayment()));
            }

            if (filter.getIsTransferPayment() != null) {
                if (filter.getIsTransferPayment()) {
                    // Solo registros donde SÍ hay imagen de comprobante
                    predicates.add(cb.isNotNull(root.get(Participant_.paymentProofImage)));
                } else {
                    // Solo registros donde NO hay imagen de comprobante
                    predicates.add(cb.isNull(root.get(Participant_.paymentProofImage)));
                }
            }

            if (filter.getIsCardPayment() != null) {
                if (filter.getIsCardPayment()) {
                    // Solo registros donde SÍ hay un link de comprobante
                    predicates.add(cb.isNotNull(root.get(Participant_.paymentProof)));
                } else {
                    // Solo registros donde NO hay un link de comprobante
                    predicates.add(cb.isNull(root.get(Participant_.paymentProof)));
                }
            }

            // Filtra por registros aprobados (true = aprobado, false = no aprobado)
            if (filter.getIsApproved() != null) {
                predicates.add(cb.equal(regStatusJoin.get(RegistrationStatus_.isApproved), filter.getIsApproved()));
            }

            // Filtra por número de comprobante (coincidencia exacta, sin distinción de
            // mayúsculas)
            if (filter.getVoucherNumber() != null) {
                predicates.add(cb.equal(cb.lower(regStatusJoin.get(RegistrationStatus_.voucherNumber)),
                        filter.getVoucherNumber().toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
