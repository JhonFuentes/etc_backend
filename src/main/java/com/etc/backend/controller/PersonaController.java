package com.etc.backend.controller;

import com.etc.backend.entity.Persona;
import com.etc.backend.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/personas")
@CrossOrigin(origins = "*")
public class PersonaController {

    @Autowired
    private PersonaRepository personaRepository;

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARIA','DOCENTE')")
    public ResponseEntity<List<Persona>> searchPersonas(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String nombres,
            @RequestParam(required = false) String apPaterno,
            @RequestParam(required = false) String apMaterno,
            @RequestParam(required = false) String ci,
            @RequestParam(required = false) Boolean estado
    ) {
        List<Persona> all = personaRepository.findAll();
        List<Persona> filtered = all.stream().filter(p -> {
            if (q != null) {
                String lower = q.toLowerCase();
                boolean matches = (p.getNombres() != null && p.getNombres().toLowerCase().contains(lower))
                        || (p.getApPaterno() != null && p.getApPaterno().toLowerCase().contains(lower))
                        || (p.getApMaterno() != null && p.getApMaterno().toLowerCase().contains(lower))
                        || (p.getCedula() != null && p.getCedula().toLowerCase().contains(lower));
                if (!matches) return false;
            }
            if (nombres != null && (p.getNombres() == null || !p.getNombres().toLowerCase().contains(nombres.toLowerCase()))) return false;
            if (apPaterno != null && (p.getApPaterno() == null || !p.getApPaterno().toLowerCase().contains(apPaterno.toLowerCase()))) return false;
            if (apMaterno != null && (p.getApMaterno() == null || !p.getApMaterno().toLowerCase().contains(apMaterno.toLowerCase()))) return false;
            if (ci != null && (p.getCedula() == null || !p.getCedula().equalsIgnoreCase(ci))) return false;
            if (estado != null && (p.getEstado() == null || !estado.equals(p.getEstado()))) return false;
            return true;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(filtered);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARIA','DOCENTE')")
    public ResponseEntity<Persona> getById(@PathVariable Integer id) {
        return personaRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
