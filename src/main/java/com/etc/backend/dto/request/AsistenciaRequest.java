package com.etc.backend.dto.request;

import java.time.LocalDate;

public class AsistenciaRequest {
    private Integer inscripcionId;
    private Integer horarioId;
    private LocalDate fecha;
    private String estado; // Presente, Ausente, Tardanza, Justificado
    private Short minutosTardanza;
    private String observaciones;

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
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
