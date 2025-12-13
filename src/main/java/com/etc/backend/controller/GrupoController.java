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
                try {
                    if (g.getDocente().getUsuario() != null && g.getDocente().getUsuario().getPersona() != null) {
                        var p = g.getDocente().getUsuario().getPersona();
                        String apP = p.getApPaterno();
                        String apM = p.getApMaterno();
                        docenteNombres = (p.getNombres() == null ? "" : p.getNombres())
                                + (apP == null ? "" : " " + apP)
                                + (apM == null ? "" : " " + apM);
                    }
                } catch (Exception ignored) {
                    // ignore lazy init
                }
            }
            if (g.getPeriodoAcademico() != null) {
                periodoId = g.getPeriodoAcademico().getId();
                periodoNombre = g.getPeriodoAcademico().getNombre();
            }
        } catch (Exception ex) {
            // ignore lazy init
        }
        GrupoSimpleResponse resp = new GrupoSimpleResponse(
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
        // populate nested objects for backward compatibility
        try {
            if (g.getMateria() != null) {
                resp.setMateria(new GrupoSimpleResponse.MateriaMini(g.getMateria().getId(), g.getMateria().getNombre()));
            }
        } catch (Exception ignored) {}
        try {
            if (g.getDocente() != null) {
                Integer did = g.getDocente().getId();
                String nombres = null;
                String apP = null;
                String apM = null;
                try {
                    if (g.getDocente().getUsuario() != null && g.getDocente().getUsuario().getPersona() != null) {
                        var p = g.getDocente().getUsuario().getPersona();
                        nombres = p.getNombres();
                        apP = p.getApPaterno();
                        apM = p.getApMaterno();
                    }
                } catch (Exception ignored) {}
                resp.setDocente(new GrupoSimpleResponse.DocenteMini(did, nombres, apP, apM));
            }
        } catch (Exception ignored) {}
        return resp;
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

