package com.etc.backend.repository;

import com.etc.backend.entity.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Integer> {
    List<Inscripcion> findByEstudianteId(Integer estudianteId);
    List<Inscripcion> findByGrupoId(Integer grupoId);
    List<Inscripcion> findByMatriculaId(Integer matriculaId);
}

