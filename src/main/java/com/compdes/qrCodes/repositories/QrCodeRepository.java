package com.compdes.qrCodes.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.compdes.participants.models.entities.Participant;
import com.compdes.qrCodes.models.entities.QrCode;

/**
 * Repositorio JPA para la entidad {@link QrCode}.
 * 
 * Proporciona operaciones de persistencia, consulta y eliminación para los
 * códigos QR registrados en el sistema, utilizando los métodos estándar de
 * {@link JpaRepository}.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-06
 */
@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, String> {
    public List<QrCode> findAllByOrderByNumberCodeAsc();

    /**
     * Obtiene el último código QR generado, basado en el valor más alto de
     * {@code numberCode}.
     * 
     * Este método busca el primer registro en la tabla de códigos QR,
     * ordenado de forma descendente por el campo {@code numberCode}.
     * 
     * @return un {@link Optional} que contiene el código QR con el número más alto,
     *         o vacío si no existen registros.
     */
    public Optional<QrCode> findFirstByOrderByNumberCodeDesc();

    /**
     * Busca el primer código QR que no esté asociado a ningún participante.
     * 
     * Este método se utiliza para recuperar un código QR disponible para
     * asignación,
     * es decir, cuya relación con {@link Participant} sea nula.
     * 
     * @return un {@link Optional} que contiene el primer código QR disponible si
     *         existe,
     *         o vacío si no se encuentra ninguno.
     */
    public Optional<QrCode> findFirstByParticipantIsNullOrderByNumberCodeAsc();

    /**
     * Busca un código QR asociado a un participante cuyo usuario tenga el nombre
     * especificado.
     * 
     * Este método permite recuperar un {@link QrCode} navegando por la relación
     * entre
     * el participante y su usuario de sistema, filtrando por el nombre de usuario
     * exacto.
     * 
     * @param username nombre de usuario asociado al participante
     * @return un {@link Optional} que contiene el código QR si existe, o vacío si
     *         no se encuentra coincidencia
     */
    public Optional<QrCode> findByParticipant_CompdesUser_Username(String username);
}
