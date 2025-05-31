package com.compdes.paymentProofs.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.compdes.paymentProofs.models.entities.PaymentProof;

/**
 * Repositorio para la gestión de entidades {@link PaymentProof}.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-05-30
 */
@Repository
public interface PaymentProofRepository extends CrudRepository<PaymentProof, String> {

}
