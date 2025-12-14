package com.etc.backend.dto.response;

import java.time.LocalDate;

public class AsistenciaResponse {
    private Integer id;
    private Integer inscripcionId;
    private Integer horarioId;
    private LocalDate fecha;
    private String estado;
    private Short minutosTardanza;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getInscripcionId() { return inscripcionId; }
    public void setInscripcionId(Integer inscripcionId) { this.inscripcionId = inscripcionId; }
    public Integer getHorarioId() { return horarioId; }
    public void setHorarioId(Integer horarioId) { this.horarioId = horarioId; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Short getMinutosTardanza() { return minutosTardanza; }
    public void setMinutosTardanza(Short minutosTardanza) { this.minutosTardanza = minutosTardanza; }
}
