package com.etc.backend.dto.response;

import java.time.LocalDate;

public class InscripcionResponse {
    private Integer id;
    private Integer estudianteId;
    private Integer grupoId;
    private Integer matriculaId;
    private LocalDate fechaInscripcion;
    private String tipoInscripcion;
    private String estado;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Integer estudianteId) { this.estudianteId = estudianteId; }
    public Integer getGrupoId() { return grupoId; }
    public void setGrupoId(Integer grupoId) { this.grupoId = grupoId; }
    public Integer getMatriculaId() { return matriculaId; }
    public void setMatriculaId(Integer matriculaId) { this.matriculaId = matriculaId; }
    public LocalDate getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(LocalDate fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }
    public String getTipoInscripcion() { return tipoInscripcion; }
    public void setTipoInscripcion(String tipoInscripcion) { this.tipoInscripcion = tipoInscripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
