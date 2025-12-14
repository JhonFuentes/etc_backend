package com.etc.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MatriculaResponse {
    private Integer id;
    private Integer estudianteId;
    private Integer carreraSedeId;
    private Integer periodoAcademicoId;
    private LocalDate fechaMatricula;
    private Short semestreCursando;
    private BigDecimal montoMatricula;
    private String estado;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Integer estudianteId) { this.estudianteId = estudianteId; }
    public Integer getCarreraSedeId() { return carreraSedeId; }
    public void setCarreraSedeId(Integer carreraSedeId) { this.carreraSedeId = carreraSedeId; }
    public Integer getPeriodoAcademicoId() { return periodoAcademicoId; }
    public void setPeriodoAcademicoId(Integer periodoAcademicoId) { this.periodoAcademicoId = periodoAcademicoId; }
    public LocalDate getFechaMatricula() { return fechaMatricula; }
    public void setFechaMatricula(LocalDate fechaMatricula) { this.fechaMatricula = fechaMatricula; }
    public Short getSemestreCursando() { return semestreCursando; }
    public void setSemestreCursando(Short semestreCursando) { this.semestreCursando = semestreCursando; }
    public BigDecimal getMontoMatricula() { return montoMatricula; }
    public void setMontoMatricula(BigDecimal montoMatricula) { this.montoMatricula = montoMatricula; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
