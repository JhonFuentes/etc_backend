package com.etc.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDashboardResponse {
    private Integer usuarioId;
    private BigDecimal promedioGeneral;
    private Integer totalInscripciones;
    private List<String> materiasInscritas;
    private String ultimoAcceso;
}
