package com.etc.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CalificacionResponse {
    private Integer id;
    private Integer inscripcionId;
    private Integer tipoEvaluacionId;
    private BigDecimal nota;
    private LocalDate fechaEvaluacion;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getInscripcionId() { return inscripcionId; }
    public void setInscripcionId(Integer inscripcionId) { this.inscripcionId = inscripcionId; }
    public Integer getTipoEvaluacionId() { return tipoEvaluacionId; }
    public void setTipoEvaluacionId(Integer tipoEvaluacionId) { this.tipoEvaluacionId = tipoEvaluacionId; }
    public BigDecimal getNota() { return nota; }
    public void setNota(BigDecimal nota) { this.nota = nota; }
    public LocalDate getFechaEvaluacion() { return fechaEvaluacion; }
    public void setFechaEvaluacion(LocalDate fechaEvaluacion) { this.fechaEvaluacion = fechaEvaluacion; }
}
