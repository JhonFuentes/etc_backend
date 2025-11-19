package com.etc.backend.controller;

import com.etc.backend.entity.Carrera;
import com.etc.backend.repository.CarreraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carreras")
@CrossOrigin(origins = "*")
public class CarreraController {

    @Autowired
    private CarreraRepository carreraRepository;

    @GetMapping
    public ResponseEntity<List<Carrera>> getAllCarreras() {
        return ResponseEntity.ok(carreraRepository.findByEstadoTrue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrera> getCarreraById(@PathVariable Integer id) {
        return carreraRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

