package com.etc.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscripciones", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"estudiante_id", "grupo_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id", nullable = false)
    private Grupo grupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matricula_id", nullable = false)
    private Matricula matricula;

    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDate fechaInscripcion = LocalDate.now();

    @Column(name = "tipo_inscripcion", length = 20)
    @Enumerated(EnumType.STRING)
    private TipoInscripcion tipoInscripcion = TipoInscripcion.Regular;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private EstadoInscripcion estado = EstadoInscripcion.Inscrito;

    @Column(name = "fecha_retiro")
    private LocalDate fechaRetiro;

    @Column(name = "motivo_retiro", columnDefinition = "TEXT")
    private String motivoRetiro;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum TipoInscripcion {
        Regular, Segunda_matrícula, Tercera_matrícula
    }

    public enum EstadoInscripcion {
        Inscrito, Retirado, Aprobado, Reprobado
    }
}

