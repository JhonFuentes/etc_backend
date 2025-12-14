package com.etc.backend.security;

import com.etc.backend.entity.Calificacion;
import com.etc.backend.entity.Docente;
import com.etc.backend.entity.Inscripcion;
import com.etc.backend.entity.Asistencia;
import com.etc.backend.entity.Grupo;
import com.etc.backend.entity.Estudiante;
import com.etc.backend.repository.CalificacionRepository;
import com.etc.backend.repository.DocenteRepository;
import com.etc.backend.repository.InscripcionRepository;
import com.etc.backend.repository.GrupoRepository;
import com.etc.backend.repository.EstudianteRepository;
import com.etc.backend.repository.AsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("securityService")
public class SecurityService {

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private CalificacionRepository calificacionRepository;

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    private Integer getUsuarioId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) return null;
        try {
            Object p = authentication.getPrincipal();
            if (p instanceof UserPrincipal) return ((UserPrincipal) p).getId();
        } catch (Exception ignored) {}
        return null;
    }

    public boolean isDocenteDelGrupo(Authentication authentication, Integer grupoId) {
        Integer uid = getUsuarioId(authentication);
        if (uid == null || grupoId == null) return false;
        Optional<Docente> od = docenteRepository.findByUsuarioId(uid);
        if (od.isEmpty()) return false;
        Optional<Grupo> og = grupoRepository.findById(grupoId);
        if (og.isEmpty()) return false;
        try {
            return og.get().getDocente() != null && og.get().getDocente().getId().equals(od.get().getId());
        } catch (Exception e) { return false; }
    }

    public boolean isDocenteOfInscripcion(Authentication authentication, Integer inscripcionId) {
        Integer uid = getUsuarioId(authentication);
        if (uid == null || inscripcionId == null) return false;
        Optional<Docente> od = docenteRepository.findByUsuarioId(uid);
        if (od.isEmpty()) return false;
        Optional<Inscripcion> oi = inscripcionRepository.findById(inscripcionId);
        if (oi.isEmpty()) return false;
        try {
            Grupo g = oi.get().getGrupo();
            return g != null && g.getDocente() != null && g.getDocente().getId().equals(od.get().getId());
        } catch (Exception e) { return false; }
    }

    public boolean isInscripcionOwnedByStudent(Authentication authentication, Integer inscripcionId) {
        Integer uid = getUsuarioId(authentication);
        if (uid == null || inscripcionId == null) return false;
        Optional<Inscripcion> oi = inscripcionRepository.findById(inscripcionId);
        if (oi.isEmpty()) return false;
        try {
            Estudiante e = oi.get().getEstudiante();
            return e != null && e.getUsuario() != null && e.getUsuario().getId().equals(uid);
        } catch (Exception ex) { return false; }
    }

    public boolean isDocenteOfCalificacion(Authentication authentication, Integer calificacionId) {
        Integer uid = getUsuarioId(authentication);
        if (uid == null || calificacionId == null) return false;
        Optional<Docente> od = docenteRepository.findByUsuarioId(uid);
        if (od.isEmpty()) return false;
        Optional<Calificacion> oc = calificacionRepository.findById(calificacionId);
        if (oc.isEmpty()) return false;
        try {
            Inscripcion ins = oc.get().getInscripcion();
            Grupo g = ins.getGrupo();
            return g != null && g.getDocente() != null && g.getDocente().getId().equals(od.get().getId());
        } catch (Exception e) { return false; }
    }

    public boolean isDocenteOfAsistencia(Authentication authentication, Integer asistenciaId) {
        Integer uid = getUsuarioId(authentication);
        if (uid == null || asistenciaId == null) return false;
        Optional<Docente> od = docenteRepository.findByUsuarioId(uid);
        if (od.isEmpty()) return false;
        Optional<Asistencia> oa = asistenciaRepository.findById(asistenciaId);
        if (oa.isEmpty()) return false;
        try {
            Inscripcion ins = oa.get().getInscripcion();
            Grupo g = ins.getGrupo();
            return g != null && g.getDocente() != null && g.getDocente().getId().equals(od.get().getId());
        } catch (Exception e) { return false; }
    }

    public boolean isEstudianteOwner(Authentication authentication, Integer estudianteId) {
        Integer uid = getUsuarioId(authentication);
        if (uid == null || estudianteId == null) return false;
        Optional<Estudiante> oe = estudianteRepository.findById(estudianteId);
        if (oe.isEmpty()) return false;
        try { return oe.get().getUsuario() != null && oe.get().getUsuario().getId().equals(uid); } catch (Exception e) { return false; }
    }
}
