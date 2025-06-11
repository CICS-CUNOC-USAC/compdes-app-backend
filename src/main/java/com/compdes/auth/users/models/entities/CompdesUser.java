package com.compdes.auth.users.models.entities;

import org.hibernate.annotations.DynamicUpdate;

import com.compdes.auth.users.enums.RolesEnum;
import com.compdes.common.models.entities.Auditor;
import com.compdes.participants.models.entities.Participant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa a un usuario dentro del sistema Compdes.
 * Incluye autenticación básica por username y password, y asignación de roles.
 * 
 * Hereda de {@link Auditor} para incluir trazabilidad de creación y
 * modificación.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-01
 */
@Entity
@DynamicUpdate
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class CompdesUser extends Auditor {

    @Column(nullable = true, unique = true, length = 50)
    private String username;

    @Column(nullable = true, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RolesEnum role;

    @OneToOne
    @JoinColumn(nullable = true)
    private Participant participant;

    public CompdesUser(String id, String username, String password, RolesEnum role, Participant participant) {
        super(id);
        this.username = username;
        this.password = password;
        this.role = role;
        this.participant = participant;
    }

}
