package com.etc.backend.controller;

import com.etc.backend.dto.request.AsistenciaRequest;
import com.etc.backend.dto.response.AsistenciaResponse;
import com.etc.backend.entity.Asistencia;
import com.etc.backend.entity.Inscripcion;
import com.etc.backend.entity.Horario;
import com.etc.backend.entity.Usuario;
import com.etc.backend.repository.AsistenciaRepository;
import com.etc.backend.repository.InscripcionRepository;
import com.etc.backend.repository.HorarioRepository;
import com.etc.backend.repository.UsuarioRepository;
import com.etc.backend.security.UserPrincipal;
import com.etc.backend.security.PermissionChecker;
import com.etc.backend.security.PermissionMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/asistencias")
@CrossOrigin(origins = "*")
public class AsistenciaController {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PermissionChecker permissionChecker;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or (hasRole('DOCENTE') and @securityService.isDocenteOfAsistencia(authentication, #req.grupoId))")
    public ResponseEntity<?> create(@RequestBody AsistenciaRequest req, Authentication authentication) {
        if (!permissionChecker.canCreate(authentication, PermissionMatrix.Module.ATTENDANCE)) {
            return ResponseEntity.status(403).body("No tiene permiso para crear asistencias");
        }
        Inscripcion ins = inscripcionRepository.findById(req.getInscripcionId()).orElse(null);
        if (ins == null) return ResponseEntity.badRequest().body("Inscripcion inválida");

        Horario h = horarioRepository.findById(req.getHorarioId()).orElse(null);
        if (h == null) return ResponseEntity.badRequest().body("Horario inválido");

        Asistencia a = new Asistencia();
        a.setInscripcion(ins);
        a.setHorario(h);
        a.setFecha(req.getFecha());
        try {
            a.setEstado(Asistencia.EstadoAsistencia.valueOf(req.getEstado()));
        } catch (Exception ignored) {}
        a.setMinutosTardanza(req.getMinutosTardanza());
        a.setObservaciones(req.getObservaciones());
        if (authentication != null) {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            usuarioRepository.findById(up.getId()).ifPresent(a::setRegistradoPor);
        }

        Asistencia saved = asistenciaRepository.save(a);
        AsistenciaResponse r = new AsistenciaResponse();
        r.setId(saved.getId());
        try { r.setInscripcionId(saved.getInscripcion() != null ? saved.getInscripcion().getId() : null); } catch (Exception ignored) {}
        try { r.setHorarioId(saved.getHorario() != null ? saved.getHorario().getId() : null); } catch (Exception ignored) {}
        r.setFecha(saved.getFecha());
        r.setEstado(saved.getEstado() != null ? saved.getEstado().name() : null);
        r.setMinutosTardanza(saved.getMinutosTardanza());
        return ResponseEntity.created(URI.create("/api/asistencias/" + saved.getId())).body(r);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('DOCENTE') and @securityService.isDocenteOfAsistencia(authentication, #id))")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody AsistenciaRequest req, Authentication authentication) {
        if (!permissionChecker.canUpdate(authentication, PermissionMatrix.Module.ATTENDANCE)) {
            return ResponseEntity.status(403).body("No tiene permiso para actualizar asistencias");
        }
        return asistenciaRepository.findById(id).map(existing -> {
            existing.setFecha(req.getFecha());
            try { existing.setEstado(Asistencia.EstadoAsistencia.valueOf(req.getEstado())); } catch (Exception ignored) {}
            existing.setMinutosTardanza(req.getMinutosTardanza());
            existing.setObservaciones(req.getObservaciones());
            Asistencia saved = asistenciaRepository.save(existing);
            AsistenciaResponse r = new AsistenciaResponse();
            r.setId(saved.getId());
            try { r.setInscripcionId(saved.getInscripcion() != null ? saved.getInscripcion().getId() : null); } catch (Exception ignored) {}
            try { r.setHorarioId(saved.getHorario() != null ? saved.getHorario().getId() : null); } catch (Exception ignored) {}
            r.setFecha(saved.getFecha());
            r.setEstado(saved.getEstado() != null ? saved.getEstado().name() : null);
            r.setMinutosTardanza(saved.getMinutosTardanza());
            return ResponseEntity.ok(r);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Integer id, Authentication authentication) {
        if (!permissionChecker.canDelete(authentication, PermissionMatrix.Module.ATTENDANCE)) {
            return ResponseEntity.status(403).body("No tiene permiso para eliminar asistencias");
        }
        return asistenciaRepository.findById(id).map(a -> {
            asistenciaRepository.delete(a);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public ResponseEntity<?> list(
            @RequestParam(required = false) Integer grupoId,
            @RequestParam(required = false) Integer inscripcionId,
            @RequestParam(required = false) Integer docenteId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String desdeFecha,
            @RequestParam(required = false) String hastaFecha
    ) {
        java.util.List<Asistencia> all = asistenciaRepository.findAll();
        java.util.List<Asistencia> filtered = all.stream().filter(a -> {
            if (inscripcionId != null && (a.getInscripcion() == null || !inscripcionId.equals(a.getInscripcion().getId()))) return false;
            if (grupoId != null) {
                try { if (a.getInscripcion() == null || a.getInscripcion().getGrupo() == null || !grupoId.equals(a.getInscripcion().getGrupo().getId())) return false; } catch (Exception ignored) {}
            }
            if (docenteId != null) {
                try { if (a.getInscripcion() == null || a.getInscripcion().getGrupo() == null || a.getInscripcion().getGrupo().getDocente() == null || !docenteId.equals(a.getInscripcion().getGrupo().getDocente().getId())) return false; } catch (Exception ignored) {}
            }
            if (estado != null && (a.getEstado() == null || !a.getEstado().name().equalsIgnoreCase(estado))) return false;
            if (desdeFecha != null) {
                try { if (a.getFecha() == null || a.getFecha().isBefore(java.time.LocalDate.parse(desdeFecha))) return false; } catch (Exception ignored) {}
            }
            if (hastaFecha != null) {
                try { if (a.getFecha() == null || a.getFecha().isAfter(java.time.LocalDate.parse(hastaFecha))) return false; } catch (Exception ignored) {}
            }
            return true;
        }).toList();
        java.util.List<AsistenciaResponse> dto = filtered.stream().map(saved -> {
            AsistenciaResponse r = new AsistenciaResponse();
            r.setId(saved.getId());
            try { r.setInscripcionId(saved.getInscripcion() != null ? saved.getInscripcion().getId() : null); } catch (Exception ignored) {}
            try { r.setHorarioId(saved.getHorario() != null ? saved.getHorario().getId() : null); } catch (Exception ignored) {}
            r.setFecha(saved.getFecha());
            r.setEstado(saved.getEstado() != null ? saved.getEstado().name() : null);
            r.setMinutosTardanza(saved.getMinutosTardanza());
            return r;
        }).toList();
        return ResponseEntity.ok(dto);
    }
}
