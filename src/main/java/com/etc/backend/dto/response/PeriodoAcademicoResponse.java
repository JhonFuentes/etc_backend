package com.etc.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PeriodoAcademicoResponse {
    private Integer id;
    private Integer gestion;
    private Short periodo;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaInicioInscripciones;
    private LocalDate fechaFinInscripciones;
    private Boolean activo;
    private Boolean estado;
    private LocalDateTime createdAt;
}
