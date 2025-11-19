package com.etc.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "estudiantes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "codigo_estudiante", unique = true, nullable = false, length = 20)
    private String codigoEstudiante;

    @Column(name = "unidad_educativa", length = 150)
    private String unidadEducativa;

    @Column(name = "año_egreso_colegio")
    private Integer añoEgresoColegio;

    @Column(name = "tipo_admision", length = 30)
    private String tipoAdmision;

    @Column(name = "fecha_admision")
    private LocalDate fechaAdmision;

    @Column(name = "estado_academico", length = 30)
    @Enumerated(EnumType.STRING)
    private EstadoAcademico estadoAcademico = EstadoAcademico.Activo;

    @Column(nullable = false)
    private Boolean estado = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum EstadoAcademico {
        Activo, Egresado, Retirado, Suspendido
    }
}

