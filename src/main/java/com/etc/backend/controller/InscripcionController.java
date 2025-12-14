package com.etc.backend.controller;

import com.etc.backend.dto.request.InscripcionRequest;
import com.etc.backend.dto.response.InscripcionResponse;
import com.etc.backend.entity.Grupo;
import com.etc.backend.entity.Inscripcion;
import com.etc.backend.entity.Matricula;
import com.etc.backend.entity.Estudiante;
import com.etc.backend.repository.GrupoRepository;
import com.etc.backend.repository.InscripcionRepository;
import com.etc.backend.repository.MatriculaRepository;
import com.etc.backend.repository.EstudianteRepository;
import com.etc.backend.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
@CrossOrigin(origins = "*")
public class InscripcionController {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARIA','DIRECTOR')")
    public ResponseEntity<List<InscripcionResponse>> listAll() {
        List<Inscripcion> list = inscripcionRepository.findAll();
        List<InscripcionResponse> dto = list.stream().map(this::toDto).toList();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAnyRole('SECRETARIA','DIRECTOR') or (hasRole('DOCENTE') and @securityService.isDocenteOfInscripcion(authentication, #id)) or (hasRole('ESTUDIANTE') and @securityService.isInscripcionOwnedByStudent(authentication, #id))")
    public ResponseEntity<InscripcionResponse> getById(@PathVariable Integer id) {
        return inscripcionRepository.findById(id).map(i -> ResponseEntity.ok(toDto(i))).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SECRETARIA') or (hasRole('ESTUDIANTE') and @securityService.isEstudianteOwner(authentication, #req.estudianteId))")
    public ResponseEntity<?> create(@RequestBody InscripcionRequest req, Authentication authentication) {
        UserPrincipal up = (UserPrincipal) authentication.getPrincipal();

        Estudiante estudiante;
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ESTUDIANTE"))) {
            estudiante = estudianteRepository.findByUsuarioId(up.getId()).orElse(null);
            if (estudiante == null) return ResponseEntity.status(403).body("Estudiante no encontrado para el usuario");
        } else {
            estudiante = estudianteRepository.findById(req.getEstudianteId()).orElse(null);
        }
        if (estudiante == null) return ResponseEntity.badRequest().body("Estudiante inválido");

        Grupo grupo = grupoRepository.findById(req.getGrupoId()).orElse(null);
        if (grupo == null) return ResponseEntity.badRequest().body("Grupo inválido");

        Matricula matricula = matriculaRepository.findById(req.getMatriculaId()).orElse(null);
        if (matricula == null) return ResponseEntity.badRequest().body("Matrícula inválida");

        Inscripcion ins = new Inscripcion();
        ins.setEstudiante(estudiante);
        ins.setGrupo(grupo);
        ins.setMatricula(matricula);

        Inscripcion saved = inscripcionRepository.save(ins);
        return ResponseEntity.created(URI.create("/api/inscripciones/" + saved.getId())).body(toDto(saved));
    }

    private InscripcionResponse toDto(Inscripcion i) {
        InscripcionResponse r = new InscripcionResponse();
        r.setId(i.getId());
        try { r.setEstudianteId(i.getEstudiante() != null ? i.getEstudiante().getId() : null); } catch (Exception ignored) {}
        try { r.setGrupoId(i.getGrupo() != null ? i.getGrupo().getId() : null); } catch (Exception ignored) {}
        try { r.setMatriculaId(i.getMatricula() != null ? i.getMatricula().getId() : null); } catch (Exception ignored) {}
        r.setFechaInscripcion(i.getFechaInscripcion());
        r.setTipoInscripcion(i.getTipoInscripcion() != null ? i.getTipoInscripcion().name() : null);
        r.setEstado(i.getEstado() != null ? i.getEstado().name() : null);
        return r;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARIA')")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody InscripcionRequest req) {
        return inscripcionRepository.findById(id).map(existing -> {
            // minimal update: allow changing estado and fechaRetiro/motivo via request if provided
            // For now, we only return 204 No Content after update (no complex mapping)
            Inscripcion saved = inscripcionRepository.save(existing);
            return ResponseEntity.ok(toDto(saved));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return inscripcionRepository.findById(id).map(i -> {
            inscripcionRepository.delete(i);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
