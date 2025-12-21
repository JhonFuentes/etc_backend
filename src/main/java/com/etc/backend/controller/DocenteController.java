package com.etc.backend.controller;

import com.etc.backend.dto.response.DocenteResponse;
import com.etc.backend.entity.Docente;
import com.etc.backend.repository.DocenteRepository;
import com.etc.backend.security.PermissionChecker;
import com.etc.backend.security.PermissionMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/docentes")
@CrossOrigin(origins = "*")
public class DocenteController {

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private PermissionChecker permissionChecker;

    private DocenteResponse toDto(Docente d) {
        Integer usuarioId = null;
        String usuarioUsername = null;
        Integer personaId = null;
        String personaNombres = null;
        String personaApellidos = null;
        try {
            if (d.getUsuario() != null) {
                usuarioId = d.getUsuario().getId();
                usuarioUsername = d.getUsuario().getUsername();
            }
            if (d.getUsuario() != null && d.getUsuario().getPersona() != null) {
                var p = d.getUsuario().getPersona();
                personaId = p.getId();
                personaNombres = p.getNombres();
                String apP = p.getApPaterno();
                String apM = p.getApMaterno();
                personaApellidos = (apP == null ? "" : apP) + (apM == null ? "" : " " + apM);
            }
        } catch (Exception ex) {
            // ignore lazy init
        }
        return new DocenteResponse(
                d.getId(),
                usuarioId,
                usuarioUsername,
                personaId,
                personaNombres,
                personaApellidos,
                d.getTituloProfesional(),
                d.getGradoAcademico(),
                d.getEspecialidad(),
                d.getFechaContratacion(),
                d.getTipoContrato(),
                d.getCurriculumUrl(),
                d.getEstado(),
                d.getCreatedAt()
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIA', 'DOCENTE')")
    public ResponseEntity<List<DocenteResponse>> getAllDocentes() {
        List<Docente> list = docenteRepository.findAll().stream()
                .filter(d -> d.getEstado() != null && d.getEstado())
                .collect(Collectors.toList());
        List<DocenteResponse> dto = list.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIA', 'DOCENTE')")
    public ResponseEntity<DocenteResponse> getDocenteById(@PathVariable Integer id) {
        return docenteRepository.findById(id)
                .filter(d -> d.getEstado() != null && d.getEstado())
                .map(d -> ResponseEntity.ok(toDto(d)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIA', 'DOCENTE')")
    public ResponseEntity<List<DocenteResponse>> searchDocentes(
            @RequestParam(required = false) String especialidad,
            @RequestParam(required = false) String tipoContrato,
            @RequestParam(required = false) Boolean estado
    ) {
        List<Docente> list = docenteRepository.findAll();
        final Boolean estadoFinal = (estado == null) ? true : estado;
        List<DocenteResponse> filtered = list.stream().filter(d -> {
            if (d.getEstado() == null || !d.getEstado().equals(estadoFinal)) return false;
            if (especialidad != null && (d.getEspecialidad() == null || !d.getEspecialidad().toLowerCase().contains(especialidad.toLowerCase()))) return false;
            if (tipoContrato != null && (d.getTipoContrato() == null || !d.getTipoContrato().toLowerCase().contains(tipoContrato.toLowerCase()))) return false;
            return true;
        }).map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(filtered);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIA')")
    public ResponseEntity<?> createDocente(@RequestBody Docente docente, Authentication auth) {
        if (!permissionChecker.canCreate(auth, PermissionMatrix.Module.TEACHERS)) {
            return ResponseEntity.status(403).body("No tiene permiso para crear docentes");
        }
        Docente saved = docenteRepository.save(docente);
        return ResponseEntity.status(201).body(toDto(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIA')")
    public ResponseEntity<?> updateDocente(@PathVariable Integer id, @RequestBody Docente docente, Authentication auth) {
        if (!permissionChecker.canUpdate(auth, PermissionMatrix.Module.TEACHERS)) {
            return ResponseEntity.status(403).body("No tiene permiso para actualizar docentes");
        }
        return docenteRepository.findById(id).map(existing -> {
            existing.setEspecialidad(docente.getEspecialidad());
            existing.setTituloProfesional(docente.getTituloProfesional());
            existing.setGradoAcademico(docente.getGradoAcademico());
            existing.setEstado(docente.getEstado());
            Docente saved = docenteRepository.save(existing);
            return ResponseEntity.ok(toDto(saved));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteDocente(@PathVariable Integer id, Authentication auth) {
        if (!permissionChecker.canDelete(auth, PermissionMatrix.Module.TEACHERS)) {
            return ResponseEntity.status(403).body("No tiene permiso para eliminar docentes");
        }
        return docenteRepository.findById(id).map(d -> {
            docenteRepository.delete(d);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
