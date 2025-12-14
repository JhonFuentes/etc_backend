package com.etc.backend.repository;

import com.etc.backend.entity.TipoEvaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoEvaluacionRepository extends JpaRepository<TipoEvaluacion, Integer> {
}
