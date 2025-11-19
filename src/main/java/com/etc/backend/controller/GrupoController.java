package com.etc.backend.controller;

import com.etc.backend.entity.Grupo;
import com.etc.backend.repository.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupos")
@CrossOrigin(origins = "*")
public class GrupoController {

    @Autowired
    private GrupoRepository grupoRepository;

    @GetMapping
    public ResponseEntity<List<Grupo>> getAllGrupos() {
        return ResponseEntity.ok(grupoRepository.findByEstadoTrue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grupo> getGrupoById(@PathVariable Integer id) {
        return grupoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/periodo/{periodoAcademicoId}")
    public ResponseEntity<List<Grupo>> getGruposByPeriodo(@PathVariable Integer periodoAcademicoId) {
        return ResponseEntity.ok(grupoRepository.findByPeriodoAcademicoId(periodoAcademicoId));
    }
}

