package com.etc.backend.repository;

import com.etc.backend.entity.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Integer> {
    List<Materia> findByEstadoTrue();
    Optional<Materia> findByCodigo(String codigo);
    List<Materia> findByCarreraSedeId(Integer carreraSedeId);
}

