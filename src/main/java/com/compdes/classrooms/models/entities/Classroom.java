package com.compdes.classrooms.models.entities;

import com.compdes.common.models.entities.Auditor;
import com.compdes.moduleUni.models.entities.ModuleUni;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Representa a un participante registrado en el sistema.
 *
 *
 * Hereda de {@link Auditor} para incluir trazabilidad de creación y
 * modificación.
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-06-05
 */
@Entity
@DynamicUpdate
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Classroom extends Auditor {

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ModuleUni moduleUni;

    @Column(nullable = false)
    private Integer capacity;

    /**
     * Constructor utilizado para crear y persistir un nuevo salon por
     * primera vez.
     *
     * @param name              nombre del salon
     * @param moduleUni         uuid del modulo al que pertenece el salon
     */
    public Classroom(String name, ModuleUni moduleUni) {
        this.name = name;
        this.moduleUni = moduleUni;
        this.capacity = 30;
    }

}
