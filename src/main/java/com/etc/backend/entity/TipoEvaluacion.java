package com.etc.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tipos_evaluacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoEvaluacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(precision = 5, scale = 2)
    private BigDecimal porcentaje;

    @Column
    private Short orden;

    @Column(nullable = false)
    private Boolean estado = true;
}

