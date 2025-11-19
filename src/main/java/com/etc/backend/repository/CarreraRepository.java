package com.etc.backend.repository;

import com.etc.backend.entity.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Integer> {
    List<Carrera> findByEstadoTrue();
    Optional<Carrera> findByCodigo(String codigo);
}

