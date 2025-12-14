package com.etc.backend.controller;

import com.etc.backend.dto.request.CalificacionRequest;
import com.etc.backend.entity.Calificacion;
import com.etc.backend.entity.Inscripcion;
import com.etc.backend.entity.TipoEvaluacion;
import com.etc.backend.entity.Usuario;
import com.etc.backend.repository.CalificacionRepository;
import com.etc.backend.repository.InscripcionRepository;
import com.etc.backend.repository.TipoEvaluacionRepository;
import com.etc.backend.repository.UsuarioRepository;
import com.etc.backend.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/calificaciones")
@CrossOrigin(origins = "*")
public class CalificacionController {

    @Autowired
    private CalificacionRepository calificacionRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private TipoEvaluacionRepository tipoEvaluacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @PreAuthorize("hasAnyRole('DOCENTE','ADMIN')")
    public ResponseEntity<?> create(@RequestBody CalificacionRequest req, Authentication authentication) {
        Inscripcion ins = inscripcionRepository.findById(req.getInscripcionId()).orElse(null);
        if (ins == null) return ResponseEntity.badRequest().body("Inscripcion inválida");

        TipoEvaluacion te = tipoEvaluacionRepository.findById(req.getTipoEvaluacionId()).orElse(null);
        if (te == null) return ResponseEntity.badRequest().body("Tipo de evaluación inválido");

        Calificacion c = new Calificacion();
        c.setInscripcion(ins);
        c.setTipoEvaluacion(te);
        c.setNota(req.getNota());
        c.setFechaEvaluacion(req.getFechaEvaluacion());

        if (authentication != null) {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            usuarioRepository.findById(up.getId()).ifPresent(c::setRegistradoPor);
        }

        Calificacion saved = calificacionRepository.save(c);
        return ResponseEntity.created(URI.create("/api/calificaciones/" + saved.getId())).body(saved);
    }
}
