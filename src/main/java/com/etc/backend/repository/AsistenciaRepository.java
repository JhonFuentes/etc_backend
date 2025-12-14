package com.etc.backend.repository;

import com.etc.backend.entity.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Integer> {
    List<Asistencia> findByInscripcionId(Integer inscripcionId);
    List<Asistencia> findByFecha(LocalDate fecha);
}
