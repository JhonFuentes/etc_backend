package com.etc.backend.controller;

import com.etc.backend.dto.response.GrupoSimpleResponse;
import com.etc.backend.entity.Grupo;
import com.etc.backend.repository.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/grupos")
@CrossOrigin(origins = "*")
public class GrupoController {

    @Autowired
    private GrupoRepository grupoRepository;

    private GrupoSimpleResponse toDto(Grupo g) {
        Integer materiaId = null;
        String materiaNombre = null;
        Integer docenteId = null;
        String docenteNombres = null;
        Integer periodoId = null;
        String periodoNombre = null;
        try {
            if (g.getMateria() != null) {
                materiaId = g.getMateria().getId();
                materiaNombre = g.getMateria().getNombre();
            }
            if (g.getDocente() != null) {
                docenteId = g.getDocente().getId();
                if (g.getDocente().getPersona() != null) {
                    docenteNombres = g.getDocente().getPersona().getNombres() + " " + g.getDocente().getPersona().getApellidos();
                }
            }
            if (g.getPeriodoAcademico() != null) {
                periodoId = g.getPeriodoAcademico().getId();
                periodoNombre = g.getPeriodoAcademico().getNombre();
            }
        } catch (Exception ex) {
            // ignore lazy init
        }
        return new GrupoSimpleResponse(
                g.getId(),
                materiaId,
                materiaNombre,
                docenteId,
                docenteNombres,
                periodoId,
                periodoNombre,
                g.getNombre(),
                g.getCupoMaximo(),
                g.getCupoActual(),
                g.getEstado(),
                g.getCreatedAt()
        );
    }

    @GetMapping
    public ResponseEntity<List<GrupoSimpleResponse>> getAllGrupos() {
        List<Grupo> list = grupoRepository.findByEstadoTrue();
        List<GrupoSimpleResponse> dto = list.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrupoSimpleResponse> getGrupoById(@PathVariable Integer id) {
        return grupoRepository.findById(id)
                .map(g -> ResponseEntity.ok(toDto(g)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/periodo/{periodoAcademicoId}")
    public ResponseEntity<List<GrupoSimpleResponse>> getGruposByPeriodo(@PathVariable Integer periodoAcademicoId) {
        List<Grupo> list = grupoRepository.findByPeriodoAcademicoId(periodoAcademicoId);
        List<GrupoSimpleResponse> dto = list.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }
}

