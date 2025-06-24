package com.compdes.registrationStatus.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compdes.participants.models.entities.Participant;
import com.compdes.registrationStatus.models.entities.RegistrationStatus;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-30
 */
@Repository
public interface RegistrationStatusRepository extends JpaRepository<RegistrationStatus, String> {

    public Boolean existsByParticipant(Participant participant);

    public Boolean existsByVoucherNumberIsNotNullAndVoucherNumber(String voucherNumber);

    /**
     * Verifica si existe un registro con el mismo número de comprobante, pero su ID no sea el especificado.
     * 
     * @param voucherNumber el número de comprobante a verificar
     * @param id            el ID que debe ser excluido de la búsqueda
     * @return {@code true} si existe otro registro con el mismo número de
     *         comprobante, {@code false} en caso contrario
     */
    public Boolean existsByVoucherNumberAndIdIsNot(String voucherNumber, String id);

    public Optional<RegistrationStatus> findByParticipant_Id(String id);
}
