package com.compdes.moduleUni.models.entities;

import com.compdes.classrooms.models.entities.Classroom;
import com.compdes.common.models.entities.Auditor;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

/**
 * Representa a un modulo de la universidad registrado en el sistema.
*
 * Hereda de {@link Auditor} para incluir trazabilidad de creación y
 * modificación.
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-06-03
 */
@Entity
@DynamicUpdate
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class ModuleUni extends Auditor{
    @Column(nullable = false, length = 50)
    private String name;

    @OneToMany
    @JoinColumn
    private List<Classroom> classrooms;

    public ModuleUni(String name) {
        this.name = name;
    }
}
