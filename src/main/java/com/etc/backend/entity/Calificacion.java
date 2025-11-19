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
@Table(name = "calificaciones", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"inscripcion_id", "tipo_evaluacion_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Calificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inscripcion_id", nullable = false)
    private Inscripcion inscripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_evaluacion_id", nullable = false)
    private TipoEvaluacion tipoEvaluacion;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal nota;

    @Column(name = "nota_maxima", precision = 5, scale = 2)
    private BigDecimal notaMaxima = new BigDecimal("100");

    @Column(name = "fecha_evaluacion")
    private LocalDate fechaEvaluacion;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrado_por")
    private Usuario registradoPor;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private EstadoCalificacion estado = EstadoCalificacion.Definitiva;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum EstadoCalificacion {
        Definitiva, Provisional, Anulada
    }
}

