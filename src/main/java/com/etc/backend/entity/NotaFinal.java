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
@Table(name = "notas_finales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotaFinal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inscripcion_id", nullable = false, unique = true)
    private Inscripcion inscripcion;

    @Column(name = "nota_final", nullable = false, precision = 5, scale = 2)
    private BigDecimal notaFinal;

    @Column(name = "nota_literal", length = 20)
    private String notaLiteral;

    @Column(nullable = false)
    private Boolean aprobado;

    @Column(name = "fecha_calculo", nullable = false)
    private LocalDate fechaCalculo = LocalDate.now();

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}

