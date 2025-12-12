package com.etc.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDashboardResponse {
    private Integer docenteId;
    private Integer usuarioId;
    private List<String> materiasQueImparte;
    private Integer totalGrupos;
}
