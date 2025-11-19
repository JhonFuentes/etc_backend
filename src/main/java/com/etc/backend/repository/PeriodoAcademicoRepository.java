package com.etc.backend.repository;

import com.etc.backend.entity.PeriodoAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodoAcademicoRepository extends JpaRepository<PeriodoAcademico, Integer> {
    List<PeriodoAcademico> findByEstadoTrue();
    List<PeriodoAcademico> findByActivoTrue();
    Optional<PeriodoAcademico> findByGestionAndPeriodo(Integer gestion, Short periodo);
}

