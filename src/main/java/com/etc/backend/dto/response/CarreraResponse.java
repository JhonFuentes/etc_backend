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
public class CarreraResponse {
    private Integer id;
    private String codigo;
    private String nombre;
    private Short duracionSemestres;
    private String tituloOtorgado;
    private Boolean estado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
