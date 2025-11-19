package com.etc.backend.controller;

import com.etc.backend.entity.Materia;
import com.etc.backend.repository.MateriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materias")
@CrossOrigin(origins = "*")
public class MateriaController {

    @Autowired
    private MateriaRepository materiaRepository;

    @GetMapping
    public ResponseEntity<List<Materia>> getAllMaterias() {
        return ResponseEntity.ok(materiaRepository.findByEstadoTrue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Materia> getMateriaById(@PathVariable Integer id) {
        return materiaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/carrera-sede/{carreraSedeId}")
    public ResponseEntity<List<Materia>> getMateriasByCarreraSede(@PathVariable Integer carreraSedeId) {
        return ResponseEntity.ok(materiaRepository.findByCarreraSedeId(carreraSedeId));
    }
}

