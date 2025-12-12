package com.etc.backend.repository;

import com.etc.backend.entity.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Integer> {
    @Query("SELECT AVG(c.nota) FROM Calificacion c WHERE c.inscripcion.estudiante.usuario.id = :usuarioId")
    BigDecimal findAverageNotaByUsuarioId(@Param("usuarioId") Integer usuarioId);

    List<Calificacion> findByInscripcionEstudianteId(Integer estudianteId);
}
