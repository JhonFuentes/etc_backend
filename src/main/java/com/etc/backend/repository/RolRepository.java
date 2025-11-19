package com.etc.backend.repository;

import com.etc.backend.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    List<Rol> findByEstadoTrue();
    Optional<Rol> findByNombre(String nombre);
}

