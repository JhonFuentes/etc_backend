package com.etc.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "matriculas", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"estudiante_id", "periodo_academico_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrera_sede_id", nullable = false)
    private CarreraSede carreraSede;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodo_academico_id", nullable = false)
    private PeriodoAcademico periodoAcademico;

    @Column(name = "fecha_matricula", nullable = false)
    private LocalDate fechaMatricula = LocalDate.now();

    @Column(name = "semestre_cursando", nullable = false)
    private Short semestreCursando;

    @Column(name = "tipo_matricula", length = 30)
    @Enumerated(EnumType.STRING)
    private TipoMatricula tipoMatricula = TipoMatricula.Regular;

    @Column(name = "monto_matricula", precision = 10, scale = 2)
    private BigDecimal montoMatricula;

    @Column(name = "monto_pagado", precision = 10, scale = 2)
    private BigDecimal montoPagado = BigDecimal.ZERO;

    @Column(name = "estado_pago", length = 20)
    @Enumerated(EnumType.STRING)
    private EstadoPago estadoPago = EstadoPago.Pendiente;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private EstadoMatricula estado = EstadoMatricula.Activa;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum TipoMatricula {
        Regular, Especial, Oyente
    }

    public enum EstadoPago {
        Pendiente, Pagado, Parcial
    }

    public enum EstadoMatricula {
        Activa, Retirada, Anulada, Finalizada
    }
}

