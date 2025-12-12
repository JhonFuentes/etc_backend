package com.etc.backend.repository;

import com.etc.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByUsernameAndEstadoTrue(String username);
    boolean existsByUsername(String username);
    long countByEstadoTrue();
}

