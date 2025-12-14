package com.etc.backend.repository;

import com.etc.backend.entity.CarreraSede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarreraSedeRepository extends JpaRepository<CarreraSede, Integer> {
}
