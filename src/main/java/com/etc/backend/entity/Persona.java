package com.etc.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "personas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, length = 20)
    private String cedula;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(name = "ap_paterno", nullable = false, length = 50)
    private String apPaterno;

    @Column(name = "ap_materno", length = 50)
    private String apMaterno;

    @Column(name = "fecha_nac", nullable = false)
    private LocalDate fechaNac;

    @Column(nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private Genero genero;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 15)
    private String telefono1;

    @Column(length = 15)
    private String telefono2;

    @Column(length = 200)
    private String domicilio;

    @Column(length = 50)
    private String ciudad;

    @Column(length = 50)
    private String pais = "Bolivia";

    @Column(name = "foto_url", length = 255)
    private String fotoUrl;

    @Column(nullable = false)
    private Boolean estado = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Genero {
        M, F, O
    }
}

