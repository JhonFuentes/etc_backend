package com.etc.backend.controller;

import com.etc.backend.dto.request.HorarioRequest;
import com.etc.backend.entity.Grupo;
import com.etc.backend.entity.Horario;
import com.etc.backend.entity.Aula;
import com.etc.backend.repository.GrupoRepository;
import com.etc.backend.repository.HorarioRepository;
import com.etc.backend.repository.AulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/horarios")
@CrossOrigin(origins = "*")
public class HorarioController {

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private AulaRepository aulaRepository;

    @GetMapping("/grupo/{grupoId}")
    public ResponseEntity<List<Horario>> getByGrupo(@PathVariable Integer grupoId) {
        return ResponseEntity.ok(horarioRepository.findByGrupoId(grupoId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SECRETARIA','DIRECTOR')")
    public ResponseEntity<?> create(@RequestBody HorarioRequest req) {
        Grupo g = grupoRepository.findById(req.getGrupoId()).orElse(null);
        if (g == null) return ResponseEntity.badRequest().body("Grupo inválido");
        Aula a = aulaRepository.findById(req.getAulaId()).orElse(null);
        if (a == null) return ResponseEntity.badRequest().body("Aula inválida");

        Horario h = new Horario();
        h.setGrupo(g);
        h.setAula(a);
        h.setDiaSemana(req.getDiaSemana());
        h.setHoraInicio(req.getHoraInicio());
        h.setHoraFin(req.getHoraFin());
        try { h.setTipo(Horario.TipoHorario.valueOf(req.getTipo())); } catch (Exception ignored) {}

        Horario saved = horarioRepository.save(h);
        return ResponseEntity.created(URI.create("/api/horarios/" + saved.getId())).body(saved);
    }
}
