package com.etc.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "asistencias", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"inscripcion_id", "horario_id", "fecha"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asistencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inscripcion_id", nullable = false)
    private Inscripcion inscripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "horario_id", nullable = false)
    private Horario horario;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EstadoAsistencia estado = EstadoAsistencia.Presente;

    @Column(name = "minutos_tardanza")
    private Short minutosTardanza;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrado_por")
    private Usuario registradoPor;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum EstadoAsistencia {
        Presente, Ausente, Tardanza, Justificado
    }
}

