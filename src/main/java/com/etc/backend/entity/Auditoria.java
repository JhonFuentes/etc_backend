package com.etc.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String tabla;

    @Column(name = "registro_id", nullable = false)
    private Integer registroId;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Accion accion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "valores_anteriores", columnDefinition = "JSONB")
    private String valoresAnteriores;

    @Column(name = "valores_nuevos", columnDefinition = "JSONB")
    private String valoresNuevos;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum Accion {
        INSERT, UPDATE, DELETE
    }
}

