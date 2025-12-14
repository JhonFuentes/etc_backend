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
    public ResponseEntity<Matricula> getById(@PathVariable Integer id) {
        return matriculaRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
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
        return ResponseEntity.created(URI.create("/api/matriculas/" + saved.getId())).body(saved);
    }
}
