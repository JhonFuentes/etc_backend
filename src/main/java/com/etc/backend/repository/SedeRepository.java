package com.etc.backend.repository;

import com.etc.backend.entity.Sede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SedeRepository extends JpaRepository<Sede, Integer> {
    List<Sede> findByEstadoTrue();
}

