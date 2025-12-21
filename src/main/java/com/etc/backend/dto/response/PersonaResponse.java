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
public class PersonaResponse {
    private Integer id;
    private String cedula;
    private String nombres;
    private String apPaterno;
    private String apMaterno;
    private LocalDate fechaNac;
    private String genero;
    private String email;
    private String telefono1;
    private String telefono2;
    private String domicilio;
    private String ciudad;
    private String pais;
    private String fotoUrl;
    private Boolean estado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
