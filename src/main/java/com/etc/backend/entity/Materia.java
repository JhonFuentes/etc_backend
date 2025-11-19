package com.etc.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "materias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Materia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrera_sede_id", nullable = false)
    private CarreraSede carreraSede;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private Short semestre;

    @Column(name = "horas_teoricas")
    private Short horasTeoricas = 0;

    @Column(name = "horas_practicas")
    private Short horasPracticas = 0;

    @Column(nullable = false)
    private Short creditos;

    @Column(name = "es_electiva", nullable = false)
    private Boolean esElectiva = false;

    @Column(nullable = false)
    private Boolean estado = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

