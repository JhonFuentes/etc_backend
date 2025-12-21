package com.etc.backend.controller;

import com.etc.backend.dto.response.SedeResponse;
import com.etc.backend.entity.Sede;
import com.etc.backend.repository.SedeRepository;
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
@RequestMapping("/api/sedes")
@CrossOrigin(origins = "*")
public class SedeController {

    @Autowired
    private SedeRepository sedeRepository;

    @Autowired
    private PermissionChecker permissionChecker;

    private SedeResponse toDto(Sede s) {
        return new SedeResponse(
                s.getId(),
                s.getNombre(),
                s.getDireccion(),
                s.getTelefono(),
                s.getEmail(),
                s.getEstado(),
                s.getCreatedAt(),
                s.getUpdatedAt()
        );
    }

    @GetMapping
    public ResponseEntity<List<SedeResponse>> getAllSedes() {
        List<Sede> list = sedeRepository.findAll().stream()
                .filter(s -> s.getEstado() != null && s.getEstado())
                .collect(Collectors.toList());
        List<SedeResponse> dto = list.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SedeResponse> getSedeById(@PathVariable Integer id) {
        return sedeRepository.findById(id)
                .filter(s -> s.getEstado() != null && s.getEstado())
                .map(s -> ResponseEntity.ok(toDto(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<SedeResponse>> searchSedes(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) Boolean estado
    ) {
        List<Sede> list = sedeRepository.findAll();
        final Boolean estadoFinal = (estado == null) ? true : estado;
        List<SedeResponse> filtered = list.stream().filter(s -> {
            if (s.getEstado() == null || !s.getEstado().equals(estadoFinal)) return false;
            if (nombre != null && (s.getNombre() == null || !s.getNombre().toLowerCase().contains(nombre.toLowerCase()))) return false;
            if (ciudad != null && (s.getDireccion() == null || !s.getDireccion().toLowerCase().contains(ciudad.toLowerCase()))) return false;
            return true;
        }).map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(filtered);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSede(@RequestBody Sede sede, Authentication auth) {
        if (!permissionChecker.canCreate(auth, PermissionMatrix.Module.CONFIGURATION)) {
            return ResponseEntity.status(403).body("No tiene permiso para crear sedes");
        }
        Sede saved = sedeRepository.save(sede);
        return ResponseEntity.status(201).body(toDto(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSede(@PathVariable Integer id, @RequestBody Sede sede, Authentication auth) {
        if (!permissionChecker.canUpdate(auth, PermissionMatrix.Module.CONFIGURATION)) {
            return ResponseEntity.status(403).body("No tiene permiso para actualizar sedes");
        }
        return sedeRepository.findById(id).map(existing -> {
            existing.setNombre(sede.getNombre());
            existing.setDireccion(sede.getDireccion());
            existing.setTelefono(sede.getTelefono());
            existing.setEmail(sede.getEmail());
            existing.setEstado(sede.getEstado());
            Sede saved = sedeRepository.save(existing);
            return ResponseEntity.ok(toDto(saved));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSede(@PathVariable Integer id, Authentication auth) {
        if (!permissionChecker.canDelete(auth, PermissionMatrix.Module.CONFIGURATION)) {
            return ResponseEntity.status(403).body("No tiene permiso para eliminar sedes");
        }
        return sedeRepository.findById(id).map(s -> {
            sedeRepository.delete(s);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
