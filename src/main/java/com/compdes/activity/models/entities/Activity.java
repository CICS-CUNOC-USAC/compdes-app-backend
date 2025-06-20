package com.compdes.activity.models.entities;

import com.compdes.activity.enums.ActivityType;
import com.compdes.classrooms.models.entities.Classroom;
import com.compdes.common.models.entities.Auditor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Activity extends Auditor {
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, length = 500)
    private String description;
    private ActivityType type;
    @Column(nullable = false)
    private LocalDateTime scheduledDate;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Classroom classroom;
}
