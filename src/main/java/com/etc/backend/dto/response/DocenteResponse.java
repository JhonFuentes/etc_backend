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
public class DocenteResponse {
    private Integer id;
    private Integer usuarioId;
    private String usuarioUsername;
    private Integer personaId;
    private String personaNombres;
    private String personaApellidos;
    private String tituloProfesional;
    private String gradoAcademico;
    private String especialidad;
    private LocalDate fechaContratacion;
    private String tipoContrato;
    private String curriculumUrl;
    private Boolean estado;
    private LocalDateTime createdAt;
}
