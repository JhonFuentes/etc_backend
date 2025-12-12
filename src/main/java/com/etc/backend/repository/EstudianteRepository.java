package com.etc.backend.repository;

import com.etc.backend.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {
    Optional<Estudiante> findByCodigoEstudiante(String codigoEstudiante);
    Optional<Estudiante> findByUsuarioId(Integer usuarioId);
    boolean existsByCodigoEstudiante(String codigoEstudiante);
    long countByEstadoTrue();
}

