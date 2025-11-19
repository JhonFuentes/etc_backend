package com.etc.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "grupos", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"materia_id", "periodo_academico_id", "nombre"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodo_academico_id", nullable = false)
    private PeriodoAcademico periodoAcademico;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(name = "cupo_maximo")
    private Integer cupoMaximo = 30;

    @Column(name = "cupo_actual")
    private Integer cupoActual = 0;

    @Column(nullable = false)
    private Boolean estado = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}

