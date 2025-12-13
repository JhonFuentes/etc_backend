package com.etc.backend.controller;

import com.etc.backend.dto.response.EstudianteSimpleResponse;
import com.etc.backend.entity.Estudiante;
import com.etc.backend.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/estudiantes")
@CrossOrigin(origins = "*")
public class EstudianteController {

    @Autowired
    private EstudianteRepository estudianteRepository;

    private EstudianteSimpleResponse toDto(Estudiante e) {
        Integer usuarioId = null;
        String username = null;
        try {
            if (e.getUsuario() != null) {
                usuarioId = e.getUsuario().getId();
                username = e.getUsuario().getUsername();
            }
        } catch (Exception ex) {
            // ignore lazy init
        }
        return new EstudianteSimpleResponse(
                e.getId(),
                usuarioId,
                username,
                e.getCodigoEstudiante(),
                e.getUnidadEducativa(),
                e.getAñoEgresoColegio(),
                e.getTipoAdmision(),
                e.getFechaAdmision() != null ? e.getFechaAdmision().toString() : null,
                e.getEstadoAcademico() != null ? e.getEstadoAcademico().name() : null,
                e.getEstado(),
                e.getCreatedAt()
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCENTE')")
    public ResponseEntity<List<EstudianteSimpleResponse>> getAllEstudiantes() {
        List<Estudiante> list = estudianteRepository.findAll();
        List<EstudianteSimpleResponse> dto = list.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteSimpleResponse> getEstudianteById(@PathVariable Integer id) {
        return estudianteRepository.findById(id)
                .map(e -> ResponseEntity.ok(toDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<EstudianteSimpleResponse> getEstudianteByCodigo(@PathVariable String codigo) {
        return estudianteRepository.findByCodigoEstudiante(codigo)
                .map(e -> ResponseEntity.ok(toDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }
}

