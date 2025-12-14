package com.etc.backend.controller;

import com.etc.backend.dto.request.MatriculaRequest;
import com.etc.backend.entity.CarreraSede;
import com.etc.backend.entity.Matricula;
import com.etc.backend.entity.Estudiante;
import com.etc.backend.entity.PeriodoAcademico;
import com.etc.backend.repository.CarreraSedeRepository;
import com.etc.backend.repository.MatriculaRepository;
import com.etc.backend.repository.EstudianteRepository;
import com.etc.backend.repository.PeriodoAcademicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/matriculas")
@CrossOrigin(origins = "*")
public class MatriculaController {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private CarreraSedeRepository carreraSedeRepository;

    @Autowired
    private PeriodoAcademicoRepository periodoAcademicoRepository;

    @GetMapping("/{id}")
    public ResponseEntity<com.etc.backend.dto.response.MatriculaResponse> getById(@PathVariable Integer id) {
        return matriculaRepository.findById(id).map(m -> {
            com.etc.backend.dto.response.MatriculaResponse r = new com.etc.backend.dto.response.MatriculaResponse();
            r.setId(m.getId());
            try { r.setEstudianteId(m.getEstudiante() != null ? m.getEstudiante().getId() : null); } catch (Exception ignored) {}
            try { r.setCarreraSedeId(m.getCarreraSede() != null ? m.getCarreraSede().getId() : null); } catch (Exception ignored) {}
            try { r.setPeriodoAcademicoId(m.getPeriodoAcademico() != null ? m.getPeriodoAcademico().getId() : null); } catch (Exception ignored) {}
            r.setFechaMatricula(m.getFechaMatricula());
            r.setSemestreCursando(m.getSemestreCursando());
            r.setMontoMatricula(m.getMontoMatricula());
            r.setEstado(m.getEstado() != null ? m.getEstado().name() : null);
            return ResponseEntity.ok(r);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARIA')")
    public ResponseEntity<?> create(@RequestBody MatriculaRequest req) {
        Estudiante estudiante = estudianteRepository.findById(req.getEstudianteId()).orElse(null);
        if (estudiante == null) return ResponseEntity.badRequest().body("Estudiante inválido");

        CarreraSede cs = carreraSedeRepository.findById(req.getCarreraSedeId()).orElse(null);
        if (cs == null) return ResponseEntity.badRequest().body("CarreraSede inválida");

        PeriodoAcademico p = periodoAcademicoRepository.findById(req.getPeriodoAcademicoId()).orElse(null);
        if (p == null) return ResponseEntity.badRequest().body("Periodo académico inválido");

        Matricula m = new Matricula();
        m.setEstudiante(estudiante);
        m.setCarreraSede(cs);
        m.setPeriodoAcademico(p);
        m.setSemestreCursando(req.getSemestreCursando());
        m.setMontoMatricula(req.getMontoMatricula());

        Matricula saved = matriculaRepository.save(m);
        com.etc.backend.dto.response.MatriculaResponse r = new com.etc.backend.dto.response.MatriculaResponse();
        r.setId(saved.getId());
        try { r.setEstudianteId(saved.getEstudiante() != null ? saved.getEstudiante().getId() : null); } catch (Exception ignored) {}
        try { r.setCarreraSedeId(saved.getCarreraSede() != null ? saved.getCarreraSede().getId() : null); } catch (Exception ignored) {}
        try { r.setPeriodoAcademicoId(saved.getPeriodoAcademico() != null ? saved.getPeriodoAcademico().getId() : null); } catch (Exception ignored) {}
        r.setFechaMatricula(saved.getFechaMatricula());
        r.setSemestreCursando(saved.getSemestreCursando());
        r.setMontoMatricula(saved.getMontoMatricula());
        r.setEstado(saved.getEstado() != null ? saved.getEstado().name() : null);
        return ResponseEntity.created(URI.create("/api/matriculas/" + saved.getId())).body(r);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARIA')")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody MatriculaRequest req) {
        return matriculaRepository.findById(id).map(existing -> {
            // minimal update allowed
            Matricula saved = matriculaRepository.save(existing);
            com.etc.backend.dto.response.MatriculaResponse r = new com.etc.backend.dto.response.MatriculaResponse();
            r.setId(saved.getId());
            try { r.setEstudianteId(saved.getEstudiante() != null ? saved.getEstudiante().getId() : null); } catch (Exception ignored) {}
            try { r.setCarreraSedeId(saved.getCarreraSede() != null ? saved.getCarreraSede().getId() : null); } catch (Exception ignored) {}
            try { r.setPeriodoAcademicoId(saved.getPeriodoAcademico() != null ? saved.getPeriodoAcademico().getId() : null); } catch (Exception ignored) {}
            r.setFechaMatricula(saved.getFechaMatricula());
            r.setSemestreCursando(saved.getSemestreCursando());
            r.setMontoMatricula(saved.getMontoMatricula());
            r.setEstado(saved.getEstado() != null ? saved.getEstado().name() : null);
            return ResponseEntity.ok(r);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return matriculaRepository.findById(id).map(m -> {
            matriculaRepository.delete(m);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
