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
public class CarreraSedeResponse {
    private Integer id;
    private Integer carreraId;
    private String carreraNombre;
    private Integer sedeId;
    private String sedeNombre;
    private Integer cupoMaximo;
    private Boolean estado;
    private LocalDateTime createdAt;
}
