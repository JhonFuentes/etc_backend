package com.etc.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EstudianteResponse {
    private Integer id;
    private Integer usuarioId;
    private String usuarioUsername;
    private Integer personaId;
    private String personaNombres;
    private String personaApellidos;
    private String codigoEstudiante;
    private String unidadEducativa;
    private Integer añoEgresoColegio;
    private String tipoAdmision;
    private LocalDate fechaAdmision;
    private String estadoAcademico;
    private Boolean estado;
    private LocalDateTime createdAt;
}
