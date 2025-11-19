package com.etc.backend.repository;

import com.etc.backend.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
    Optional<Persona> findByCedula(String cedula);
    Optional<Persona> findByEmail(String email);
    boolean existsByCedula(String cedula);
    boolean existsByEmail(String email);
}

