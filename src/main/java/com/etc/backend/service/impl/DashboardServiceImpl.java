package com.etc.backend.service.impl;

import com.etc.backend.dto.response.GeneralDashboardResponse;
import com.etc.backend.dto.response.StudentDashboardResponse;
import com.etc.backend.dto.response.TeacherDashboardResponse;
import com.etc.backend.entity.Estudiante;
import com.etc.backend.entity.Grupo;
import com.etc.backend.repository.*;
import com.etc.backend.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private CalificacionRepository calificacionRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public StudentDashboardResponse getStudentDashboard(Integer usuarioId) {
        Estudiante estudiante = estudianteRepository.findByUsuarioId(usuarioId).orElse(null);
        if (estudiante == null) return null;

        BigDecimal promedio = calificacionRepository.findAverageNotaByUsuarioId(usuarioId);
        if (promedio == null) promedio = BigDecimal.ZERO;

        Integer estudianteId = estudiante.getId();
        int totalInscripciones = inscripcionRepository.findByEstudianteId(estudianteId).size();

        List<String> materias = inscripcionRepository.findByEstudianteId(estudianteId).stream()
                .map(i -> i.getGrupo().getMateria().getNombre())
                .distinct()
                .collect(Collectors.toList());

        String ultimoAcceso = estudiante.getUsuario() != null ? (estudiante.getUsuario().getUltimoAcceso() != null ? estudiante.getUsuario().getUltimoAcceso().toString() : null) : null;

        return new StudentDashboardResponse(usuarioId, promedio, totalInscripciones, materias, ultimoAcceso);
    }

    @Override
    public TeacherDashboardResponse getTeacherDashboard(Integer usuarioId) {
        return docenteRepository.findByUsuarioId(usuarioId)
                .map(docente -> {
                    List<Grupo> grupos = grupoRepository.findByDocenteId(docente.getId());
                    List<String> materias = grupos.stream()
                            .map(g -> g.getMateria().getNombre())
                            .distinct()
                            .collect(Collectors.toList());
                    return new TeacherDashboardResponse(docente.getId(), usuarioId, materias, grupos.size());
                }).orElse(null);
    }

    @Override
    public GeneralDashboardResponse getGeneralDashboard() {
        long estudiantesActivos = estudianteRepository.countByEstadoTrue();
        long docentesActivos = docenteRepository.countByEstadoTrue();
        long usuariosTotal = usuarioRepository.count();
        long materiasActivas = materiaRepository.findByEstadoTrue().size();
        long gruposActivos = grupoRepository.findByEstadoTrue().size();

        return new GeneralDashboardResponse(estudiantesActivos, docentesActivos, usuariosTotal, materiasActivas, gruposActivos);
    }
}
