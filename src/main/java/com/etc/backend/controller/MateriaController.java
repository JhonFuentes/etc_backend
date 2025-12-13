package com.etc.backend.controller;

import com.etc.backend.dto.response.MateriaSimpleResponse;
import com.etc.backend.entity.Materia;
import com.etc.backend.repository.MateriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/materias")
@CrossOrigin(origins = "*")
public class MateriaController {

    @Autowired
    private MateriaRepository materiaRepository;

    private MateriaSimpleResponse toDto(Materia m) {
        Integer carreraSedeId = null;
        if (m.getCarreraSede() != null) {
            try {
                carreraSedeId = m.getCarreraSede().getId();
            } catch (Exception e) {
                // ignore lazy init issues, keep id null
            }
        }
        return new MateriaSimpleResponse(
                m.getId(),
                carreraSedeId,
                m.getCodigo(),
                m.getNombre(),
                m.getSemestre(),
                m.getHorasTeoricas(),
                m.getHorasPracticas(),
                m.getCreditos(),
                m.getEsElectiva(),
                m.getEstado()
        );
    }

    @GetMapping
    public ResponseEntity<List<MateriaSimpleResponse>> getAllMaterias() {
        List<Materia> list = materiaRepository.findByEstadoTrue();
        List<MateriaSimpleResponse> dto = list.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MateriaSimpleResponse> getMateriaById(@PathVariable Integer id) {
        return materiaRepository.findById(id)
                .map(m -> ResponseEntity.ok(toDto(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/carrera-sede/{carreraSedeId}")
    public ResponseEntity<List<MateriaSimpleResponse>> getMateriasByCarreraSede(@PathVariable Integer carreraSedeId) {
        List<Materia> list = materiaRepository.findByCarreraSedeId(carreraSedeId);
        List<MateriaSimpleResponse> dto = list.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }
}

