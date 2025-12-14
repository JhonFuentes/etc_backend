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
    public ResponseEntity<List<com.etc.backend.dto.response.AulaResponse>> listAll() {
        List<Aula> list = aulaRepository.findAll();
        List<com.etc.backend.dto.response.AulaResponse> dto = list.stream().map(a -> {
            com.etc.backend.dto.response.AulaResponse r = new com.etc.backend.dto.response.AulaResponse();
            r.setId(a.getId());
            try { r.setSedeId(a.getSede() != null ? a.getSede().getId() : null); } catch (Exception ignored) {}
            r.setNombre(a.getNombre());
            r.setCapacidad(a.getCapacidad());
            r.setTipo(a.getTipo());
            r.setEstado(a.getEstado());
            r.setCreatedAt(a.getCreatedAt());
            return r;
        }).toList();
        return ResponseEntity.ok(dto);
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
        com.etc.backend.dto.response.AulaResponse r = new com.etc.backend.dto.response.AulaResponse();
        r.setId(saved.getId());
        try { r.setSedeId(saved.getSede() != null ? saved.getSede().getId() : null); } catch (Exception ignored) {}
        r.setNombre(saved.getNombre());
        r.setCapacidad(saved.getCapacidad());
        r.setTipo(saved.getTipo());
        r.setEstado(saved.getEstado());
        r.setCreatedAt(saved.getCreatedAt());
        return ResponseEntity.created(URI.create("/api/aulas/" + saved.getId())).body(r);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARIA')")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody AulaRequest req) {
        return aulaRepository.findById(id).map(existing -> {
            existing.setNombre(req.getNombre());
            existing.setCapacidad(req.getCapacidad());
            existing.setTipo(req.getTipo());
            Aula saved = aulaRepository.save(existing);
            com.etc.backend.dto.response.AulaResponse r = new com.etc.backend.dto.response.AulaResponse();
            r.setId(saved.getId());
            try { r.setSedeId(saved.getSede() != null ? saved.getSede().getId() : null); } catch (Exception ignored) {}
            r.setNombre(saved.getNombre());
            r.setCapacidad(saved.getCapacidad());
            r.setTipo(saved.getTipo());
            r.setEstado(saved.getEstado());
            r.setCreatedAt(saved.getCreatedAt());
            return ResponseEntity.ok(r);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return aulaRepository.findById(id).map(a -> {
            aulaRepository.delete(a);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
