package com.etc.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioSimpleResponse {
    private Integer id;
    private String username;
    private Integer rolId;
    private String rolNombre;
    private Integer personaId;
    private String personaNombres;
    private String personaApellidos;
    private LocalDateTime ultimoAcceso;
    private Short intentosFallidos;
    private Boolean bloqueado;
    private Boolean estado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
