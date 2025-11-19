package com.etc.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_academico", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"estudiante_id", "periodo_academico_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialAcademico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodo_academico_id", nullable = false)
    private PeriodoAcademico periodoAcademico;

    @Column(name = "promedio_periodo", precision = 5, scale = 2)
    private BigDecimal promedioPeriodo;

    @Column(name = "promedio_acumulado", precision = 5, scale = 2)
    private BigDecimal promedioAcumulado;

    @Column(name = "creditos_periodo")
    private Integer creditosPeriodo;

    @Column(name = "creditos_acumulados")
    private Integer creditosAcumulados;

    @Column(name = "materias_aprobadas")
    private Integer materiasAprobadas;

    @Column(name = "materias_reprobadas")
    private Integer materiasReprobadas;

    @Column(name = "fecha_calculo", nullable = false)
    private LocalDate fechaCalculo = LocalDate.now();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}

