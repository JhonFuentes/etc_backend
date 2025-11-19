package com.etc.backend.repository;

import com.etc.backend.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {
    List<Matricula> findByEstudianteId(Integer estudianteId);
    List<Matricula> findByPeriodoAcademicoId(Integer periodoAcademicoId);
    Optional<Matricula> findByEstudianteIdAndPeriodoAcademicoId(Integer estudianteId, Integer periodoAcademicoId);
}

