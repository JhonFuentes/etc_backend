package com.etc.backend.repository;

import com.etc.backend.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Integer> {
    List<Grupo> findByEstadoTrue();
    List<Grupo> findByPeriodoAcademicoId(Integer periodoAcademicoId);
    List<Grupo> findByDocenteId(Integer docenteId);
    List<Grupo> findByMateriaId(Integer materiaId);
}

