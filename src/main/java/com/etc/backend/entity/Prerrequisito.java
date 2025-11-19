package com.etc.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prerrequisitos", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"materia_id", "materia_prerrequisito_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prerrequisito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_prerrequisito_id", nullable = false)
    private Materia materiaPrerrequisito;

    @Column(name = "es_obligatorio", nullable = false)
    private Boolean esObligatorio = true;
}

