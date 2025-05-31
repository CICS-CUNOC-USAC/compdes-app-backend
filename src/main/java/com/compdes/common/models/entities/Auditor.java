
package com.compdes.common.models.entities;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Auditor {

    /**
     * Identificador único de la entidad. Se genera automáticamente utilizando
     * el tipo UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false, unique = true, length = 36)
    private String id;

    /**
     * Fecha y hora en que se creó el registro. Este campo es asignado
     * automáticamente y no puede ser actualizado.
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Fecha y hora de la última actualización del registro.
     */
    @Column
    @LastModifiedDate
    private Instant updatedAt;

    /**
     * Fecha y hora en que el registro fue eliminado lógicamente.
     */
    @Column
    private Instant deletedAt;

    /**
     * Fecha y hora en que el registro fue desactivado.
     */
    @Column
    private Instant desactivatedAt;

    public Auditor(String id) {
        this.id = id;
    }

}
