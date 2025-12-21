package com.etc.backend.controller;

import com.etc.backend.dto.response.CarreraSedeResponse;
import com.etc.backend.entity.CarreraSede;
import com.etc.backend.repository.CarreraSedeRepository;
import com.etc.backend.security.PermissionChecker;
import com.etc.backend.security.PermissionMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/carreras-sede")
@CrossOrigin(origins = "*")
public class CarreraSedeController {

    @Autowired
    private CarreraSedeRepository carreraSedeRepository;

    @Autowired
    private PermissionChecker permissionChecker;

    private CarreraSedeResponse toDto(CarreraSede cs) {
        Integer carreraId = null;
        String carreraNombre = null;
        Integer sedeId = null;
        String sedeNombre = null;
        try {
            if (cs.getCarrera() != null) {
                carreraId = cs.getCarrera().getId();
                carreraNombre = cs.getCarrera().getNombre();
            }
            if (cs.getSede() != null) {
                sedeId = cs.getSede().getId();
                sedeNombre = cs.getSede().getNombre();
            }
        } catch (Exception ex) {
            // ignore lazy init
        }
        return new CarreraSedeResponse(
                cs.getId(),
                carreraId,
                carreraNombre,
                sedeId,
                sedeNombre,
                cs.getCupoMaximo(),
                cs.getEstado(),
                cs.getCreatedAt()
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIA')")
    public ResponseEntity<List<CarreraSedeResponse>> getAllCarrerasSede() {
        List<CarreraSede> list = carreraSedeRepository.findAll().stream()
                .filter(cs -> cs.getEstado() != null && cs.getEstado())
                .collect(Collectors.toList());
        List<CarreraSedeResponse> dto = list.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIA')")
    public ResponseEntity<CarreraSedeResponse> getCarreraSedeById(@PathVariable Integer id) {
        return carreraSedeRepository.findById(id)
                .filter(cs -> cs.getEstado() != null && cs.getEstado())
                .map(cs -> ResponseEntity.ok(toDto(cs)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'SECRETARIA')")
    public ResponseEntity<List<CarreraSedeResponse>> searchCarrerasSede(
            @RequestParam(required = false) Integer carreraId,
            @RequestParam(required = false) Integer sedeId,
            @RequestParam(required = false) Boolean estado
    ) {
        List<CarreraSede> list = carreraSedeRepository.findAll();
        final Boolean estadoFinal = (estado == null) ? true : estado;
        List<CarreraSedeResponse> filtered = list.stream().filter(cs -> {
            if (cs.getEstado() == null || !cs.getEstado().equals(estadoFinal)) return false;
            if (carreraId != null && (cs.getCarrera() == null || !carreraId.equals(cs.getCarrera().getId()))) return false;
            if (sedeId != null && (cs.getSede() == null || !sedeId.equals(cs.getSede().getId()))) return false;
            return true;
        }).map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(filtered);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCarreraSede(@RequestBody CarreraSede carreraSede, Authentication auth) {
        if (!permissionChecker.canCreate(auth, PermissionMatrix.Module.CONFIGURATION)) {
            return ResponseEntity.status(403).body("No tiene permiso para crear carrera-sede");
        }
        CarreraSede saved = carreraSedeRepository.save(carreraSede);
        return ResponseEntity.status(201).body(toDto(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCarreraSede(@PathVariable Integer id, @RequestBody CarreraSede carreraSede, Authentication auth) {
        if (!permissionChecker.canUpdate(auth, PermissionMatrix.Module.CONFIGURATION)) {
            return ResponseEntity.status(403).body("No tiene permiso para actualizar carrera-sede");
        }
        return carreraSedeRepository.findById(id).map(existing -> {
            existing.setCupoMaximo(carreraSede.getCupoMaximo());
            existing.setEstado(carreraSede.getEstado());
            CarreraSede saved = carreraSedeRepository.save(existing);
            return ResponseEntity.ok(toDto(saved));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCarreraSede(@PathVariable Integer id, Authentication auth) {
        if (!permissionChecker.canDelete(auth, PermissionMatrix.Module.CONFIGURATION)) {
            return ResponseEntity.status(403).body("No tiene permiso para eliminar carrera-sede");
        }
        return carreraSedeRepository.findById(id).map(cs -> {
            carreraSedeRepository.delete(cs);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
