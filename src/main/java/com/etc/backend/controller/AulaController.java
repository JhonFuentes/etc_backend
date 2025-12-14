package com.etc.backend.controller;

import com.etc.backend.dto.request.AulaRequest;
import com.etc.backend.entity.Aula;
import com.etc.backend.entity.Sede;
import com.etc.backend.repository.AulaRepository;
import com.etc.backend.repository.SedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/aulas")
@CrossOrigin(origins = "*")
public class AulaController {

    @Autowired
    private AulaRepository aulaRepository;

    @Autowired
    private SedeRepository sedeRepository;

    @GetMapping
    public ResponseEntity<List<Aula>> listAll() {
        return ResponseEntity.ok(aulaRepository.findAll());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARIA')")
    public ResponseEntity<?> create(@RequestBody AulaRequest req) {
        Sede s = sedeRepository.findById(req.getSedeId()).orElse(null);
        if (s == null) return ResponseEntity.badRequest().body("Sede inválida");

        Aula a = new Aula();
        a.setSede(s);
        a.setNombre(req.getNombre());
        a.setCapacidad(req.getCapacidad());
        a.setTipo(req.getTipo());

        Aula saved = aulaRepository.save(a);
        return ResponseEntity.created(URI.create("/api/aulas/" + saved.getId())).body(saved);
    }
}
