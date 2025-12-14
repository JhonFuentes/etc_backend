package com.etc.backend.dto.request;

import java.math.BigDecimal;

public class MatriculaRequest {
    private Integer estudianteId;
    private Integer carreraSedeId;
    private Integer periodoAcademicoId;
    private Short semestreCursando;
    private BigDecimal montoMatricula;

    public Integer getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Integer estudianteId) { this.estudianteId = estudianteId; }
    public Integer getCarreraSedeId() { return carreraSedeId; }
    public void setCarreraSedeId(Integer carreraSedeId) { this.carreraSedeId = carreraSedeId; }
    public Integer getPeriodoAcademicoId() { return periodoAcademicoId; }
    public void setPeriodoAcademicoId(Integer periodoAcademicoId) { this.periodoAcademicoId = periodoAcademicoId; }
    public Short getSemestreCursando() { return semestreCursando; }
    public void setSemestreCursando(Short semestreCursando) { this.semestreCursando = semestreCursando; }
    public BigDecimal getMontoMatricula() { return montoMatricula; }
    public void setMontoMatricula(BigDecimal montoMatricula) { this.montoMatricula = montoMatricula; }
}
