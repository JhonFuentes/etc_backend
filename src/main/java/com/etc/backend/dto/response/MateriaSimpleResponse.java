package com.etc.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MateriaSimpleResponse {
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
}
