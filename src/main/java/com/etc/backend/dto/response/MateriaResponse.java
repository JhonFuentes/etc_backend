package com.etc.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MateriaResponse {
    private Integer id;
    private Integer carreraSedeId;
    private String codigo;
    private String nombre;
    private Short semestre;
    private Short horasTeoricas;
    private Short horasPracticas;
    private Short creditos;
    private Boolean esElectiva;
    private Boolean estado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
