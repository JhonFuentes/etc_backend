package com.etc.backend.controller;

import com.etc.backend.dto.request.InscripcionRequest;
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
    public ResponseEntity<List<Inscripcion>> listAll() {
        return ResponseEntity.ok(inscripcionRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inscripcion> getById(@PathVariable Integer id) {
        return inscripcionRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARIA','ESTUDIANTE')")
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
        return ResponseEntity.created(URI.create("/api/inscripciones/" + saved.getId())).body(saved);
    }
}
