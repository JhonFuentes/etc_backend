package com.etc.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralDashboardResponse {
    private long estudiantesActivos;
    private long docentesActivos;
    private long usuariosTotal;
    private long materiasActivas;
    private long gruposActivos;
}
