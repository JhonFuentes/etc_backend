package com.etc.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteSimpleResponse {
    private Integer id;
    private Integer usuarioId;
    private String username;
    private String codigoEstudiante;
    private String unidadEducativa;
    private Integer anoEgresoColegio;
    private String tipoAdmision;
    private String fechaAdmision;
    private String estadoAcademico;
    private Boolean estado;
    private LocalDateTime createdAt;
}
