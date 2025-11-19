package com.etc.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "docentes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Docente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "titulo_profesional", nullable = false, length = 150)
    private String tituloProfesional;

    @Column(name = "grado_academico", length = 50)
    private String gradoAcademico;

    @Column(length = 100)
    private String especialidad;

    @Column(name = "fecha_contratacion")
    private LocalDate fechaContratacion;

    @Column(name = "tipo_contrato", length = 30)
    private String tipoContrato;

    @Column(name = "curriculum_url", length = 255)
    private String curriculumUrl;

    @Column(nullable = false)
    private Boolean estado = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}

