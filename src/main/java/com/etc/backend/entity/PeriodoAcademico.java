package com.etc.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "periodos_academicos", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"gestion", "periodo"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeriodoAcademico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer gestion;

    @Column(nullable = false)
    private Short periodo;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "fecha_inicio_inscripciones")
    private LocalDate fechaInicioInscripciones;

    @Column(name = "fecha_fin_inscripciones")
    private LocalDate fechaFinInscripciones;

    @Column(nullable = false)
    private Boolean activo = false;

    @Column(nullable = false)
    private Boolean estado = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}

