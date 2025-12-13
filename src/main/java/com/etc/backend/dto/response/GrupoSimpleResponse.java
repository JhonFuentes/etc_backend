package com.etc.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrupoSimpleResponse {
    private Integer id;
    private Integer materiaId;
    private String materiaNombre;
    private Integer docenteId;
    private String docenteNombres;
    private Integer periodoAcademicoId;
    private String periodoAcademicoNombre;
    private String nombre;
    private Integer cupoMaximo;
    private Integer cupoActual;
    private Boolean estado;
    private LocalDateTime createdAt;
}
